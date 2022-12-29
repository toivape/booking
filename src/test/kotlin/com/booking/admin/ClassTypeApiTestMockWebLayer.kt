package com.booking.admin

import com.booking.PostgreExtension
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

/**
 * Test DB and Service layers through Mock Http layer.
 * Individual beans can be mocked if needed.
 * Database test container is configured using @ExtendWith.
 */
// See for more about integration/junit testing: https://www.arhohuttunen.com/spring-boot-integration-testing/
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgreExtension::class)
class ClassTypeApiTestMockWebLayer(@Autowired val mockMvc: MockMvc, @Autowired val mapper: ObjectMapper, @Autowired val classTypeRepo: ClassTypeRepo, @Autowired val classDefinitionRepo: ClassDefinitionRepo) {

    @AfterEach
    fun cleanAfterEach() {
        classDefinitionRepo.deleteAll()
        classTypeRepo.deleteAll()
    }

    @Sql(
        statements = [
            "INSERT INTO class_type (code, name) VALUES ('A1','Aloittelijat 1')",
            "INSERT INTO class_type (code, name) values ('MYSORE', 'Mysoreharjoittelu')"
        ]
    )
    @Test
    fun `Find class types`() {
        mockMvc.get("/api/classes/types")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath("\$.[0].code") { value("A1") }
                jsonPath("\$.[0].name") { value("Aloittelijat 1") }
                jsonPath("\$.[1].code") { value("MYSORE") }
                jsonPath("\$.[1].name") { value("Mysoreharjoittelu") }
            }
    }

    @Sql(
        statements = [
            "INSERT INTO class_type (code, name) values ('MYSORE', 'Mysoreharjoittelu')",
            "INSERT INTO class_definition (id, version, name, location, class_type_code, price_credits, max_people, description ,recurrence_days , recurrence_start_date, recurrence_end_date, start_time, end_time, created_at, updated_at) VALUES                 (1, 1, 'Astanga mysore', 'Annankatu 29 B Sisäpiha / Courtyard, 00100 Helsinki, Suomi', 'MYSORE', 10, 16, 'Mysoreharjoittelu on perinteinen tapa harjoitella astangajoogaa.', ARRAY['MA', 'KE'], '2023-01-01', '2023-05-31', '08:00', '09:15', NOW(), NOW())"
        ]
    )
    @Test
    fun `List class definitions`() {
        mockMvc.get("/api/classes/definitions")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath("\$.[0].id") { value(1) }
                jsonPath("\$.[0].name") { value("Astanga mysore") }
            }
    }

    @Test
    fun `New class type is added to DB`() {
        val form = ClassTypeForm("NEW", "New type")

        mockMvc.post("/api/classes/types") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isCreated() }
            jsonPath("\$.code") { value(form.code) }
            jsonPath("\$.name") { value(form.name) }
        }

        mockMvc.get("/api/classes/types/${form.code}")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath("\$.code") { value(form.code) }
                jsonPath("\$.name") { value(form.name) }
            }
    }

    @ParameterizedTest
    @ValueSource(strings = ["BADCHAR!", "SPA CE", "DAS-H", "lower"])
    fun `Add class type with invalid code returns bad request`() {
        val form = ClassTypeForm("INVALID!", "New type")
        mockMvc.post("/api/classes/types") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("\$.code") { value("must match \"${ClassTypeForm.CODE_PATTERN}\"") }
        }
    }
}

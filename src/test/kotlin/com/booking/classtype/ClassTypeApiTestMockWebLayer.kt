package com.booking.classtype

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
class ClassTypeApiTestMockWebLayer(@Autowired val mockMvc: MockMvc, @Autowired val mapper: ObjectMapper, @Autowired val classTypeRepo: ClassTypeRepo) {

    @AfterEach
    fun cleanAfterEach() {
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
        mockMvc.get("/api/classtypes")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath("\$.[0].code") { value("A1") }
                jsonPath("\$.[0].name") { value("Aloittelijat 1") }
                jsonPath("\$.[1].code") { value("MYSORE") }
                jsonPath("\$.[1].name") { value("Mysoreharjoittelu") }
            }
    }

    @Test
    fun `New class type is added to DB`() {
        val form = ClassTypeForm("NEW", "New type")

        mockMvc.post("/api/classtypes") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isCreated() }
            jsonPath("\$.code") { value(form.code) }
            jsonPath("\$.name") { value(form.name) }
        }

        mockMvc.get("/api/classtypes/${form.code}")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath("\$.code") { value(form.code) }
                jsonPath("\$.name") { value(form.name) }
            }
    }

    @ParameterizedTest
    @ValueSource(strings = ["BADCHAR!", "SPA CE", "DAS-H", "lower"])
    fun `Add class type with invalid code returns bad request`(code: String) {
        val form = ClassTypeForm(code, "New type")
        mockMvc.post("/api/classtypes") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("\$.code") { value("must match \"${ClassTypeForm.CODE_PATTERN}\"") }
        }
    }
}

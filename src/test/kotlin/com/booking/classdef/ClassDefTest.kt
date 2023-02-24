package com.booking.classdef

import com.booking.PostgreExtension
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgreExtension::class)
@Sql(
    statements = [
        "INSERT INTO class_type (code, name) values ('MYSORE', 'Mysoreharjoittelu')",
        "INSERT INTO class_definition (id, version, name, location, class_type_code, price_credits, max_people, description, recurrence_days, recurrence_start_date, recurrence_end_date, start_time, end_time, created_at, updated_at) VALUES (1, 1, 'Astanga mysore', 'Annankatu 29 B Sisäpiha / Courtyard, 00100 Helsinki, Suomi', 'MYSORE', 10, 16, 'Mysoreharjoittelu on perinteinen tapa harjoitella astangajoogaa.', ARRAY['MON', 'WED'], '2023-01-01', '2023-05-31', '08:00', '09:15', NOW(), NOW())",
        "INSERT INTO class_definition (id, version, name, location, class_type_code, price_credits, max_people, description, recurrence_days, recurrence_start_date, recurrence_end_date, start_time, end_time, created_at, updated_at) VALUES (99, 1, 'Poistuva kurssi', null, 'MYSORE', 10, 16, null, null, null, null, null, null, NOW(), NOW())"
    ]
)
@Sql(
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
    statements = ["DELETE FROM class_definition", "DELETE FROM class_type"]
)
class ClassDefTest(@Autowired val mockMvc: MockMvc, @Autowired val mapper: ObjectMapper) {

    @Test
    fun `List class definitions`() {
        mockMvc.get("/api/classdefs")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath("\$.[0].id") { value(1) }
                jsonPath("\$.[0].name") { value("Astanga mysore") }
                jsonPath("\$.[0].priceCredits") { value(10) }
                jsonPath("\$.[0].classType.code") { value("MYSORE") }
            }
    }

    @Test
    fun `Delete class definition`() {
        mockMvc.delete("/api/classdefs/99")
            .andExpect { status { isOk() } }
    }

    @Test
    fun `Get class definition`() {
        mockMvc.get("/api/classdefs/1")
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("\$.id") { value(1) } }
    }

    @Test
    fun `When classTypeCode is invalid save class definition fails`() {
        val form = ClassDefinitionForm(name = "Bad class", classTypeCode = "im-so-sore")

        mockMvc.post("/api/classdefs") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("\$.classTypeCode") { value("must match \"^[A-Z0-9_]+\"") }
        }
    }

    @Test
    fun `When end date is before start date then save class definition fails`() {
        val form = ClassDefinitionForm(
            name = "Bad dates",
            classTypeCode = "MYSORE",
            recurrenceStartDate = LocalDate.of(2023, 7, 31),
            recurrenceEndDate = LocalDate.of(2023, 1, 2)
        )

        mockMvc.post("/api/classdefs") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("\$.classDefinitionForm") { value("Recurrence start date must be before end date and start time must be before end time") }
        }
    }

    @Test
    fun `When class type does not exist save class definition fails`() {
        val form = ClassDefinitionForm(name = "Bad type", classTypeCode = "NO_EXIST")

        mockMvc.post("/api/classdefs") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("\$.errorMessage") { value("Unknown classTypeCode NO_EXIST") }
        }
    }

    @Test
    fun `Class definition is saved successfully`() {
        val form = ClassDefinitionForm(name = "Good class", classTypeCode = "MYSORE")

        mockMvc.post("/api/classdefs") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isCreated() }
            jsonPath("\$.id") { value(100) }
            jsonPath("\$.version") { value(1) }
        }
    }
}

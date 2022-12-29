package com.booking.classdef

import com.booking.PostgreExtension
import com.booking.classtype.ClassTypeRepo
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgreExtension::class)
class ClassDefTest(@Autowired val mockMvc: MockMvc, @Autowired val mapper: ObjectMapper, @Autowired val classDefRepo: ClassDefRepo, @Autowired val classTypeRepo: ClassTypeRepo) {

    @AfterEach
    fun cleanAfterEach() {
        classDefRepo.deleteAll()
        classTypeRepo.deleteAll()
    }

    @Sql(
        statements = [
            "INSERT INTO class_type (code, name) values ('MYSORE', 'Mysoreharjoittelu')",
            "INSERT INTO class_definition (id, version, name, location, class_type_code, price_credits, max_people, description, recurrence_days, recurrence_start_date, recurrence_end_date, start_time, end_time, created_at, updated_at) VALUES (1, 1, 'Astanga mysore', 'Annankatu 29 B Sisäpiha / Courtyard, 00100 Helsinki, Suomi', 'MYSORE', 10, 16, 'Mysoreharjoittelu on perinteinen tapa harjoitella astangajoogaa.', ARRAY['MON', 'WED'], '2023-01-01', '2023-05-31', '08:00', '09:15', NOW(), NOW())"
        ]
    )
    @Test
    fun `List class definitions`() {
        mockMvc.get("/api/classdefs")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath("\$.[0].id") { value(1) }
                jsonPath("\$.[0].name") { value("Astanga mysore") }
            }
    }
}

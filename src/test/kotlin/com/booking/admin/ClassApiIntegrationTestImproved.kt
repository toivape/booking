package com.booking.admin

import com.booking.PostgreExtension
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
class ClassApiIntegrationTestImproved(@Autowired val mockMvc: MockMvc) {

    @Sql(
        statements = [
            "INSERT INTO class_type (code, name) VALUES ('A1','Aloittelijat 1')",
            "INSERT INTO class_type (code, name) VALUES ('A2','Aloittelijat 2')"
        ]
    )
    @Test
    fun `Find class types`() {
        mockMvc.get("/api/classtypes")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath("\$.[0].code") { value("A1") }
                jsonPath("\$.[0].name") { value("Aloittelijat 1") }
                jsonPath("\$.[1].code") { value("A2") }
                jsonPath("\$.[1].name") { value("Aloittelijat 2") }
            }
    }
}

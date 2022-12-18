package com.booking.admin

import com.booking.postgres
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Configure test container as companion object.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ClassApiIntegrationTestFirstAttempt(@Autowired val mockMvc: MockMvc) {

    companion object {
        @Container
        val container = postgres()

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.datasource.username", container::getUsername)
        }
    }

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

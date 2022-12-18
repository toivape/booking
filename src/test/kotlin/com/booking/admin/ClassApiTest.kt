package com.booking.admin

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
class ClassApiTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var service: ClassService

    @Test
    fun `Find class types`() {
        every { service.getClassTypes() } returns listOf(ClassType("A1","Aloittelijat 1"), ClassType("A2","Aloittelijat 2"))

        mockMvc.get("/api/classtypes")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect {
                jsonPath("\$.[0].code") { value("A1") }
                jsonPath("\$.[0].name") { value("Aloittelijat 1") }
                jsonPath("\$.[1].code") { value("A2") }
                jsonPath("\$.[1].name") { value("Aloittelijat 2") }
            }

        verify(exactly = 1) { service.getClassTypes() }
    }
}

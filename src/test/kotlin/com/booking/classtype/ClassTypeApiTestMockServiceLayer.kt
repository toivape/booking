package com.booking.classtype

import com.booking.classdef.ClassDefService
import com.booking.classtype.ClassTypeForm.Companion.CODE_PATTERN
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

/**
 * Test only web layer. Service layer is mocked.
 */
@WebMvcTest
class ClassTypeApiTestMockServiceLayer(@Autowired val mockMvc: MockMvc, @Autowired val mapper: ObjectMapper) {

    @MockkBean
    private lateinit var service: ClassTypeService

    @MockkBean
    private lateinit var classDefService: ClassDefService

    @Test
    fun `Find class types`() {
        every { service.listClassTypes() } returns listOf(ClassType("A1", "Aloittelijat 1"), ClassType("A2", "Aloittelijat 2"))

        mockMvc.get("/api/classtypes")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect {
                jsonPath("\$.[0].code") { value("A1") }
                jsonPath("\$.[0].name") { value("Aloittelijat 1") }
                jsonPath("\$.[1].code") { value("A2") }
                jsonPath("\$.[1].name") { value("Aloittelijat 2") }
            }

        verify(exactly = 1) { service.listClassTypes() }
    }

    @Test
    fun `Add class type`() {
        val form = ClassTypeForm("NEW", "New type")
        every { service.saveClassType(form.code, form.name) } returns ClassType(form.code, form.name)

        mockMvc.post("/api/classtypes") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isCreated() }
            jsonPath("\$.code") { value(form.code) }
            jsonPath("\$.name") { value(form.name) }
        }

        verify(exactly = 1) { service.saveClassType(form.code, form.name) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["BADCHAR!", "SPA CE", "DAS-H", "lower"])
    fun `Add class type with invalid code fails`() {
        val form = ClassTypeForm("INVALID!", "New type")
        mockMvc.post("/api/classtypes") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("\$.code") { value("must match \"$CODE_PATTERN\"") }
        }
    }

    @Test
    fun `Adding class type with invalid name and code return error for both`() {
        val form = ClassTypeForm("123456789012345678901234567890X", "  ")
        mockMvc.post("/api/classtypes") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(form)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("\$.code") { value("size must be between 1 and 30") }
            jsonPath("\$.name") { value("must not be blank") }
        }
    }

    @Test
    fun `Delete class type with invalid code format returns error`() {
        mockMvc.delete("/api/classtypes/DELETE_ME!").andExpect {
            status { isBadRequest() }
            jsonPath("\$[0]") { value("deleteClassType.code - must match \"${ClassTypeForm.CODE_PATTERN}\"") }
        }
    }

    @Test
    fun `Getting class type using invalid code format returns error`() {
        mockMvc.get("/api/classtypes/BADCODE!").andExpect {
            status { isBadRequest() }
            jsonPath("\$[0]") { value("getClassType.code - must match \"${ClassTypeForm.CODE_PATTERN}\"") }
        }
    }
}

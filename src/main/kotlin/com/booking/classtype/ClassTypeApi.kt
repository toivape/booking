package com.booking.classtype

import arrow.core.getOrHandle
import com.booking.classtype.ClassTypeForm.Companion.CODE_PATTERN
import jakarta.persistence.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/classtypes")
@Validated // Required to validate path variables
class ClassApi(val classTypeService: ClassTypeService) {

    @GetMapping
    fun listClassTypes() = classTypeService.listClassTypes()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addClassType(
        @Valid // Is required to validate RequestBody even if class level has @Validated
        @RequestBody
        form: ClassTypeForm
    ) = classTypeService.saveClassType(form.code, form.name)

    @DeleteMapping("{code}")
    fun deleteClassType(
        @PathVariable
        @Size(max = 30)
        @Pattern(regexp = CODE_PATTERN)
        code: String
    ) = classTypeService.deleteClassType(code)

    @GetMapping("{code}")
    fun getClassType(
        @PathVariable
        @Size(max = 30)
        @Pattern(regexp = CODE_PATTERN)
        code: String
    ) = classTypeService.getClassType(code).getOrHandle {
        logger.info { "Failed to get ClassType with code $code: ${it.message}" }
        throw it
    }
}

data class ClassTypeForm(
    @field:NotBlank
    @field:Size(min = 1, max = 30)
    @field:Pattern(regexp = CODE_PATTERN)
    val code: String,

    @field:NotBlank
    @field:Size(min = 1, max = 300)
    val name: String
) {
    companion object {
        const val CODE_PATTERN = "^[A-Z0-9_]+"
    }
}

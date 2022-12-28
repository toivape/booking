package com.booking.admin

import com.booking.admin.ClassTypeForm.Companion.CODE_PATTERN
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
@RequestMapping("/api/classes")
@Validated // Required to validate path variables
class ClassApi(val classService: ClassService) {

    @GetMapping("/types")
    fun listClassTypes() = classService.listClassTypes()

    @PostMapping("/types")
    @ResponseStatus(HttpStatus.CREATED)
    fun addClassType(
        @Valid // Is required to validate RequestBody even if class level has @Validated
        @RequestBody
        classTypeForm: ClassTypeForm
    ) = classService.saveClassType(classTypeForm.code, classTypeForm.name)

    @DeleteMapping("/types/{code}")
    fun deleteClassType(
        @PathVariable
        @Size(max = 30)
        @Pattern(regexp = CODE_PATTERN)
        code: String
    ) = classService.deleteClassType(code)

    @GetMapping("/types/{code}")
    fun getClassType(
        @PathVariable
        @Size(max = 30)
        @Pattern(regexp = CODE_PATTERN)
        code: String
    ): ClassType {
        logger.info { "Get class type with code $code or throw not found" }
        return classService.getClassType(code)
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
        const val CODE_PATTERN = "^[A-Za-z0-9_]+"
    }
}
package com.booking.admin

import jakarta.persistence.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer

@RestController
@RequestMapping("/api/classes")
class ClassApi(val classService: ClassService) {

    @GetMapping("/types")
    fun listClassTypes() = classService.listClassTypes()

    @PostMapping("/types")
    @ResponseStatus(HttpStatus.CREATED)
    fun addClassType(
        @Valid @RequestBody
        classTypeForm: ClassTypeForm
    ) = classService.saveClassType(classTypeForm.code, classTypeForm.name)

    @DeleteMapping("/types/{code}")
    fun deleteClassType(@PathVariable code: String) = classService.deleteClassType(code)

    @GetMapping("/types/{code}")
    fun getClassType(@PathVariable code: String) = classService.getClassType(code)

    @GetMapping("/definitions")
    fun listClassDefinitions() = classService.listClassDefinitions()
}

@ResponseStatus(HttpStatus.NOT_FOUND)
@ExceptionHandler(EmptyResultDataAccessException::class)
fun handleValidationExceptions(
    ex: MethodArgumentNotValidException
): Map<String, String?>? {
    val errors: MutableMap<String, String?> = HashMap()
    ex.bindingResult.allErrors.forEach(
        Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage
        }
    )
    return errors
}

data class ClassTypeForm(

    // not empty max len 30
    @field:NotBlank
    val code: String,

    // not empty max len 300
    @field:NotBlank
    val name: String
)

/*
class AddClassDefinitionForm(

    @NotBlank
    var name: String?,

    @NotBlank
    var classTypeCode: String?,

    var location: String?,

    var priceCredits: Int?,

    var maxPeople: Int?,

    var description: String?,

    var recurrenceDays: Set<DayNameEnum>?,

    // start date must be before end date if both are given
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") onko pelkät dateformat ja timeformat olemassa? validointi must
    var recurrenceStartDate: LocalDate?,

    var recurrenceEndDate: LocalDate?,

    var startTime: String?,

    var endTime: String?

)
*/
enum class DayNameEnum {
    MON, TUE, WED, THU, FRI, SAT, SUN
}

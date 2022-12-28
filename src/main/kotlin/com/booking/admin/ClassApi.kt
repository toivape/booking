package com.booking.admin

import jakarta.persistence.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

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

data class ClassTypeForm(

    // not empty max len 30
    @field:NotEmpty
    @field:Size(min = 1, max = 30)
    val code: String,

    // not empty max len 300
    @field:NotEmpty
    @field:Size(min = 1, max = 300)
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

package com.booking.admin

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/classes")
class ClassDefApi(val classService: ClassService) {

    @GetMapping("/definitions")
    fun listClassDefinitions() = classService.listClassDefinitions()

    @GetMapping("/definitions/{id}")
    fun getClassDefinition(@PathVariable id: Int) = classService.getClassDefinition(id)

    @PostMapping("/definitions")
    @ResponseStatus(HttpStatus.CREATED)
    fun addClassDefinition(
        @Valid // Is required to validate RequestBody even if class level has @Validated
        @RequestBody
        form: ClassDefinitionForm
    ) = classService.saveClassDefinition(form)
}

data class ClassDefinitionForm(
    val id: Int? = null,
    val version: Int? = null,

    @field:NotBlank
    var name: String,

    @field:NotBlank
    var classTypeCode: String,

    var location: String?,

    var priceCredits: Int?,

    var maxPeople: Int?,

    var description: String?,

    @Suppress("ArrayInDataClass")
    @field:RecurrenceDays
    var recurrenceDays: Array<DayNameEnum>?,

    // start date must be before end date if both are given
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var recurrenceStartDate: LocalDate?,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    var recurrenceEndDate: LocalDate?,

    @field:DateTimeFormat(pattern = "HH:mm")
    var startTime: String?,

    @field:DateTimeFormat(pattern = "HH:mm")
    var endTime: String?

)

enum class DayNameEnum {
    MON, TUE, WED, THU, FRI, SAT, SUN
}

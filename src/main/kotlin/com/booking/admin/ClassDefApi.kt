package com.booking.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/classes")
class ClassDefApi(val classService: ClassService) {

    @GetMapping("/definitions")
    fun listClassDefinitions() = classService.listClassDefinitions()
}

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

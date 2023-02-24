package com.booking.classdef

import arrow.core.getOrHandle
import com.booking.classtype.ValidClassTypeCode
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import mu.KotlinLogging
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/classdefs")
@Validated // Required to validate path variables
class ClassDefApi(val classDefService: ClassDefService) {

    @GetMapping
    fun listClassDefinitions() = classDefService.listClassDefs()

    @GetMapping("{id}")
    fun getClassDefinition(
        @PathVariable @Min(0)
        id: Int
    ) = classDefService.getClassDef(id).getOrHandle {
        logger.error { "Failed to get ClassDefinition with id $id: ${it.message}" }
        throw it
    }

    @DeleteMapping("{id}")
    fun deleteClassDefinition(
        @PathVariable @Min(0)
        id: Int
    ) = classDefService.deleteClassDef(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createClassDef(
        @Valid // Is required to validate RequestBody even if class level has @Validated
        @RequestBody
        form: ClassDefinitionForm
    ) = classDefService.createClassDef(form).getOrHandle {
        logger.error(it) { "Failed to save class definition $form: ${it.message}" }
        throw it
    }

    @PutMapping("{id}")
    fun updateClassDef(
        @PathVariable @Min(0) id: Int,
        @Valid @RequestBody form: ClassDefinitionForm
    ) = classDefService.updateClassDef(id, form).getOrHandle {
        logger.error(it) { "Failed to save class definition $form: ${it.message}" }
        throw it
    }
}

@ValidRecurrenceTime
data class ClassDefinitionForm(
    val id: Int? = null,
    val version: Int? = null,

    @field:NotBlank
    @field:Size(min = 1, max = ClassDefinition.NAME_MAX_LEN)
    var name: String,

    @field:ValidClassTypeCode
    var classTypeCode: String,

    @field:Size(min = 1, max = ClassDefinition.LOCATION_MAX_LEN)
    var location: String? = null,

    var priceCredits: Int? = null,

    var maxPeople: Int? = null,

    @field:Size(min = 1, max = ClassDefinition.DESC_MAX_LEN)
    var description: String? = null,

    @Suppress("ArrayInDataClass")
    @field:ValidRecurrenceWeekdays
    var recurrenceWeekDays: Array<DayNameEnum>? = null,

    // start date must be before end date if both are given
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var recurrenceStartDate: LocalDate? = null,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    var recurrenceEndDate: LocalDate? = null,

    @field:DateTimeFormat(pattern = "HH:mm")
    var startTime: String? = null,

    @field:DateTimeFormat(pattern = "HH:mm")
    var endTime: String? = null

)

enum class DayNameEnum {
    MON, TUE, WED, THU, FRI, SAT, SUN
}

fun Array<DayNameEnum>.toStringArray() = this.map { it.name }.toTypedArray()

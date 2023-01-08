package com.booking.classdef

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
import java.time.LocalDate.EPOCH
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

/**
 * Validate that startDate + startTime is before endDate + endTime.
 * This is class level annotation.
 */
@Constraint(validatedBy = [RecurrenceTimeValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValidRecurrenceTime(
    val message: String = "Recurrence start date must be before end date and start time must be before end time",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class RecurrenceTimeValidator : ConstraintValidator<ValidRecurrenceTime, ClassDefinitionForm> {

    override fun isValid(value: ClassDefinitionForm?, context: ConstraintValidatorContext?): Boolean {
        // Leave null-checking to @NotNull on individual parameters
        if (value == null) {
            return true
        }

        val datesAreValid = if (datesArePresent(value)) {
            value.recurrenceStartDate!!.isBefore(value.recurrenceEndDate)
        } else {
            true
        }

        val timesAreValid = if (timesArePresent(value)) {
            val start = value.startTime!!.withDate(EPOCH)
            val end = value.endTime!!.withDate(EPOCH)
            start.isBefore(end)
        } else {
            true
        }

        return datesAreValid && timesAreValid
    }

    private fun datesArePresent(value: ClassDefinitionForm) =
        value.recurrenceStartDate != null && value.recurrenceEndDate != null

    private fun timesArePresent(value: ClassDefinitionForm) =
        value.startTime?.isNotBlank() == true && value.endTime?.isNotBlank() == true
}

fun String.withDate(dt: LocalDate): LocalDateTime {
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalTime.parse(this, timeFormat)!!
    return LocalDateTime.of(dt, time)
}

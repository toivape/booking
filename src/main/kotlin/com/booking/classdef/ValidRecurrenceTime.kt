package com.booking.classdef

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
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
    val message: String = "Recurrence start date and time must be before end date",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class RecurrenceTimeValidator : ConstraintValidator<ValidRecurrenceTime, ClassDefinitionForm> {

    override fun isValid(form: ClassDefinitionForm?, context: ConstraintValidatorContext?): Boolean {
        // Ignore check if form or dates are null
        // Leave null-checking to @NotNull on individual parameters
        if (form?.recurrenceStartDate == null || form.recurrenceEndDate == null){
            return true
        }

        // No times set -> Validate only dates
        if (form.startTime.isNullOrBlank() || form.endTime.isNullOrBlank()) {
            return form.recurrenceStartDate!!.isBefore(form.recurrenceEndDate)
        }

        // Validate dates with time
        val start = form.recurrenceStartDate!!.withTime(form.startTime!!)
        val end = form.recurrenceEndDate!!.withTime(form.endTime!!)
        return start.isBefore(end)
    }


}

fun LocalDate.withTime(timeString: String): LocalDateTime {
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalTime.parse(timeString, timeFormat)!!
    return LocalDateTime.of(this, time)
}
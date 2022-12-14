package com.booking.classdef

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [RecurrenceDayValidator::class])
@Target(AnnotationTarget.FIELD)
// @Retention(RetentionPolicy.RUNTIME)
annotation class ValidRecurrenceWeekdays(
    // val message: String = "{javax.validation.constraints.NotBlank.message}",
    val message: String = "Invalid recurrence days",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class RecurrenceDayValidator : ConstraintValidator<ValidRecurrenceWeekdays, Array<DayNameEnum>> {

    override fun isValid(value: Array<DayNameEnum>?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return true

        val duplicates = value.filter { dayName -> value.count { it == dayName } > 1 }
        return duplicates.isEmpty()
    }
}

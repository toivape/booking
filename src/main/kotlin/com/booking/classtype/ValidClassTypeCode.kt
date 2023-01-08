package com.booking.classtype

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlin.reflect.KClass

@NotBlank
@Size(min = 1, max = 30)
@Pattern(regexp = ClassTypeForm.CODE_PATTERN)
// @CheckCase(CaseMode.UPPER)
@MustBeDocumented
// @Constraint(validatedBy = [RecurrenceDayValidator::class])
@Constraint(validatedBy = [])
@Target(AnnotationTarget.FIELD)
annotation class ValidClassTypeCode(
    val message: String = "Invalid class type",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
package com.booking.classdef

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.LocalDate.EPOCH
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class ValidRecurrenceTimeTest {

    @Test
    fun `Create LocalDateTime withTime`() {
        val dt = LocalDate.parse("2023-02-15")
        val result = "12:35".withDate(dt)
        result shouldBe LocalDateTime.parse("2023-02-15T12:35:00")
    }

    @Test
    fun `Create LocalDateTime withTime fails`() {
        shouldThrow<DateTimeParseException> {
            "25:35".withDate(EPOCH)
        }.message.shouldContain("Invalid value for HourOfDay")
    }

    private val validator = RecurrenceTimeValidator()

    @ParameterizedTest
    @MethodSource("recurrenceChecks")
    fun `Validate recurrence date and time`(testValue: ClassDefinitionForm?, expectedResult: Boolean, msg: String) {
        withClue(msg) {
            validator.isValid(value = testValue, context = null).shouldBe(expectedResult)
        }
    }

    companion object {
        @JvmStatic
        fun recurrenceChecks() = listOf(
            Arguments.of(toForm(), true, "No start date/time or end date/time is valid"),
            Arguments.of(toForm(startDate = "2023-02-15", startTime = "12:30", endDate = "2023-07-15", endTime = "17:30"), true, "starts before ends is valid"),
            Arguments.of(toForm(startDate = "2023-07-15", startTime = "17:30", endDate = "2023-02-15", endTime = "12:30"), false, "starts after ends is invalid"),
            Arguments.of(toForm(startDate = "2023-02-15", endDate = "2023-07-15"), true, "startDate before endDate is valid"),
            Arguments.of(toForm(startDate = "2023-07-15", endDate = "2023-02-15"), false, "startDate after endDate is invalid"),
            Arguments.of(toForm(startTime = "12:30", endTime = "17:30"), true, "startTime before endTime is valid"),
            Arguments.of(toForm(startTime = "17:30", endTime = "12:30"), false, "startTime after endTime is invalid")
        )

        private fun toForm(startDate: String? = null, startTime: String? = null, endDate: String? = null, endTime: String? = null) = ClassDefinitionForm(
            name = "X",
            classTypeCode = "X",
            recurrenceStartDate = if (startDate != null) LocalDate.parse(startDate) else null,
            startTime = startTime,
            recurrenceEndDate = if (endDate != null) LocalDate.parse(endDate) else null,
            endTime = endTime
        )
    }
}

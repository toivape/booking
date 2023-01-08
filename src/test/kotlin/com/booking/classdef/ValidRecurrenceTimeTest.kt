package com.booking.classdef

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class ValidRecurrenceTimeTest {

    @Test
    fun `Create LocalDateTime withTime`() {
        val result = LocalDate
            .parse("2023-02-15")
            .withTime("12:35")
        result shouldBe LocalDateTime.parse("2023-02-15T12:35:00")
    }

    @Test
    fun `Create LocalDateTime withTime fails`() {
        shouldThrow<DateTimeParseException> {
            LocalDate
                .parse("2023-02-15")
                .withTime("25:35")
        }.message.shouldContain("Invalid value for HourOfDay")
    }
}

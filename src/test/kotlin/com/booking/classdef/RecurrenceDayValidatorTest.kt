package com.booking.classdef

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class RecurrenceDayValidatorTest {

    private val validator = RecurrenceDayValidator()

    @Test
    fun `Duplicate days not allowed`() {
        val dayNames = arrayOf(DayNameEnum.MON, DayNameEnum.TUE, DayNameEnum.TUE, DayNameEnum.WED)
        validator.isValid(value = dayNames, context = null).shouldBeFalse()
    }

    @ParameterizedTest
    @MethodSource("validDayNames")
    fun `Valid day name lists`(testValue: Array<DayNameEnum>?, expectedResult: Boolean) {
        validator.isValid(value = testValue, context = null).shouldBe(expectedResult)
    }

    companion object {
        @JvmStatic
        fun validDayNames() = listOf(
            Arguments.of(DayNameEnum.values(), true),
            Arguments.of(emptyArray<DayNameEnum>(), true),
            Arguments.of(null, true),
            Arguments.of(arrayOf(DayNameEnum.WED, DayNameEnum.MON), true)
        )
    }
}

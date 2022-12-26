package com.booking

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class TestContainerTest {

    companion object {
        @Container
        val container = postgres()
    }

    @Test
    fun `Container is up and running`() {
        Assertions.assertTrue(container.isRunning)
    }
}

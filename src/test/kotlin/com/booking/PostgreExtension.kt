package com.booking

import mu.KotlinLogging
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

private val logger = KotlinLogging.logger {}

class PostgreExtension : BeforeAllCallback, AfterAllCallback {

    private var container = postgres()

    override fun beforeAll(context: ExtensionContext?) {
        container.start()
        logger.info { "🫡Test container started at URL ${container.jdbcUrl}" }
        System.setProperty("spring.datasource.url", container.jdbcUrl)
        System.setProperty("spring.datasource.username", container.username)
        System.setProperty("spring.datasource.password", container.password)
    }

    override fun afterAll(context: ExtensionContext?) {
        // Nothing to do. Test container will shut itself down.
    }
}

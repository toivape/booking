package com.booking

import mu.KotlinLogging
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

private val logger = KotlinLogging.logger {}

// From https://github.com/kotlin-hands-on/spring-time-in-kotlin-episode5/blob/master/src/test/kotlin/demo/common.kt
// PostgreSQLContainer uses Java recursive generics. Kotlin does not implement them so here type needs to be set to Nothing for now (2022-12-18)
// 1.5.30 (doesn't work yet: One type argument expected for class JdbcDatabaseContainer<SELF : JdbcDatabaseContainer<SELF!>!>)

fun postgres() =
    PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine"))
        .apply {
            withDatabaseName("booking-test")
            withUsername("ultimate")
            withPassword("test")
            withInitScript("sql/schema.sql")
        }

class PostgreExtension : BeforeAllCallback, AfterAllCallback {

    private var container = postgres()

    override fun beforeAll(context: ExtensionContext?) {
        container.start()
        logger.info { "🫡PostgreSQL test container started at URL ${container.jdbcUrl}" }
        System.setProperty("spring.datasource.url", container.jdbcUrl)
        System.setProperty("spring.datasource.username", container.username)
        System.setProperty("spring.datasource.password", container.password)
    }

    override fun afterAll(context: ExtensionContext?) {
        // Nothing to do. Test container will shut itself down.
    }
}

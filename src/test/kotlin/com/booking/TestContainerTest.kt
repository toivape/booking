package com.booking

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

// From https://github.com/kotlin-hands-on/spring-time-in-kotlin-episode5/blob/master/src/test/kotlin/demo/common.kt
/*fun postgres(options: JdbcDatabaseContainer<Nothing>.() -> Unit) =
    PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply(options)*/
// PostgreSQLContainer uses Java recursive generics. They are not implemented in Kotlin so here type needs to be set to Nothing for now (2022-12-18)
// 1.5.30 (doesn't work yet: One type argument expected for class JdbcDatabaseContainer<SELF : JdbcDatabaseContainer<SELF!>!>)
// fun postgresContainer(version: String, options: JdbcDatabaseContainer.() -> Unit) =
//    PostgreSQLContainer(DockerImageName.parse("postgres:$version")).apply(options)
fun postgres() =
    PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine"))
        .apply {
            withDatabaseName("booking-test")
            withUsername("ultimate")
            withPassword("test")
            withInitScript("sql/schema.sql")
        }

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

package com.booking.admin

import com.booking.PostgreExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * Test with full spring stack from DB to Web. Database test container is configured using @ExtendWith.
 * 'webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT' causes real embedded web server to start with full Spring stack.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreExtension::class)
@AutoConfigureWebTestClient // This annotation and Webflux import required for WebTestClient to work: implementation("org.springframework.boot:spring-boot-starter-webflux")
class ClassTypeApiTestFullStack(@Autowired val webClient: WebTestClient) {

    @Sql(
        statements = [
            "INSERT INTO class_type (code, name) VALUES ('A1','Aloittelijat 1')",
            "INSERT INTO class_type (code, name) VALUES ('A2','Aloittelijat 2')"
        ]
    )
    @Test
    fun `List all class types`() {
        webClient.get().uri("/api/classes/types")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("\$.[0].code").isEqualTo("A1")
    }

    @Sql(statements = ["INSERT INTO class_type (code, name) VALUES ('MATTI','Matti')"])
    @Test
    fun `Get class type by Id`() {
        webClient.get().uri("/api/classes/types/MATTI")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("\$.code").isEqualTo("MATTI")
    }

    @Test
    fun `Class type is not found`() {
        webClient.get().uri("/api/classes/types/TEPPO")
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("\$.error").isEqualTo("Not Found")
    }

    @Test
    fun `Add class type`() {
        val body = ClassTypeForm("CODE_7", "Koodi 7")
        webClient.post().uri("/api/classes/types")
            .bodyValue(body)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("\$.code").isEqualTo(body.code)
            .jsonPath("\$.name").isEqualTo(body.name)
    }

    @Sql(statements = ["INSERT INTO class_type (code, name) VALUES ('DELETE_ME','Delete me')"])
    @Test
    fun `Delete class type`() {
        webClient.delete().uri("/api/classes/types/DELETE_ME")
            .exchange()
            .expectStatus().isOk
    }
}

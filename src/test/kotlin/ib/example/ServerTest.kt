package ib.example

import io.vertx.core.Vertx.vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.kotlin.core.deployVerticleAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
internal class ServerTest {

    private val vertx = vertx()

    @BeforeEach
    internal fun setUp() = runBlocking<Unit> {
        vertx.deployVerticleAwait(Server())
    }

    @Test
    internal fun sendRequests() {
        val testContext = VertxTestContext()

        val numberOfRequests = 10
        val checkpoint = testContext.checkpoint(numberOfRequests)
        val httpClient = WebClient.create(vertx)
        (1..numberOfRequests).forEach { n ->
            httpClient.get(8080, "localhost", "/hello?name=$n")
                .send { checkpoint.flag() }
        }

        testContext.awaitCompletion(10, TimeUnit.SECONDS)
    }
}

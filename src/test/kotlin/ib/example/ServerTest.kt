package ib.example

import io.vertx.core.Vertx.vertx
import io.vertx.core.logging.Log4j2LogDelegateFactory
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.client.WebClient
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.ext.web.client.sendAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
internal class ServerTest {

    val vertx = vertx()

    @BeforeEach
    internal fun setUp() {
        System.setProperty(
            LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME,
            Log4j2LogDelegateFactory::class.qualifiedName!!
        )
        vertx.deployVerticle(Server())
    }

    @Test
    internal fun sendRequests() = runBlocking {
        val httpClient = WebClient.create(vertx)
        (1..3).forEach { n ->
            httpClient.get(8080, "localhost", "/hello?name=$n")
                .sendAwait()
        }
    }
}

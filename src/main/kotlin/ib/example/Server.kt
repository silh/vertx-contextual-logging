package ib.example

import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.slf4j.MDC
import java.util.concurrent.ThreadLocalRandom

class Server : CoroutineVerticle() {
    private val log = LoggerFactory.getLogger(Server::class.java)

    override suspend fun start() {
        val helloRouter = Router.router(vertx)
        helloRouter
            .get("/hello")
            .handler { req ->
                val reqId = createRequestId()
                MDC.put("requestId", reqId)

                val name: String = req.request().getParam("name") ?: "anonymous"
                log.info("Hello $name.")
                vertx.executeBlocking<Unit>(
                    { promise ->
                        Thread.sleep(100)
                        log.info("Finished waiting for $name")
                        promise.complete()
                    },
                    {
                        log.info("Bye $name.")
                        MDC.remove("requestId")
                        req.response().end()
                    }
                )
            }
        vertx.createHttpServer()
            .requestHandler(helloRouter)
            .listenAwait(8080)
        log.info("Listening on port 8080")
    }

    private fun createRequestId() = ThreadLocalRandom.current().nextLong().toString(16)

}

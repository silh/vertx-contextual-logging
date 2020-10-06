package ib.example

import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.slf4j.MDCContext
import org.slf4j.MDC
import java.util.concurrent.ThreadLocalRandom

class Server : CoroutineVerticle() {
    private val log = LoggerFactory.getLogger(Server::class.java)

    override suspend fun start() {
        val helloRouter = Router.router(vertx)
        helloRouter.get("/hello")
            .handler { req ->
                val reqId = generateRequestId()
                MDC.put("requestId", reqId)
                launch(context = MDCContext()) {
                    val name: String = req.request().getParam("name") ?: "anonymous"
                    log.info("Hello $name.")
                    delay(ThreadLocalRandom.current().nextLong(100, 1000))
                    log.info("Finished waiting for $name")
                    log.info("Bye $name.")
                    req.response().end()
                }
                MDC.remove("requestId")
            }
        vertx.createHttpServer()
            .requestHandler(helloRouter)
            .listenAwait(8080)
        log.info("Listening on port 8080")
    }

    private fun generateRequestId() = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE).toString(16)

}

package ib.example

import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.apache.logging.log4j.ThreadContext
import java.util.concurrent.ThreadLocalRandom

class Server : CoroutineVerticle() {
    private val log = LoggerFactory.getLogger(Server::class.java)

    override suspend fun start() {
        val helloRouter = Router.router(vertx)
        helloRouter
            .get("/hello")
            .handler { req ->
                val reqId = ThreadLocalRandom.current().nextLong().toString()
                ThreadContext.put("requestId", reqId)

                val name: String = req.request().getParam("name") ?: "anonymous"
                log.info("Hello $name.")
                vertx.executeBlocking<Unit>(
                    {
                        Thread.sleep(100)
                        log.info("Finished waiting for $name")
                        log.info("Bye $name.")
                        ThreadContext.remove("requestId")
                        req.response().end()
                    },
                    { }
                )
            }
        vertx.createHttpServer()
            .requestHandler(helloRouter)
            .listenAwait(8080)
        log.info("Listening on port 8080")
    }

}

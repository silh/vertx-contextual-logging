package ib.example

import io.vertx.core.Vertx.vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.logging.SLF4JLogDelegateFactory

fun main() {
    // Use log4j2 for vert.x logging
    System.setProperty(
        LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME,
        SLF4JLogDelegateFactory::class.qualifiedName!!
    )

    vertx().deployVerticle(Server())
}


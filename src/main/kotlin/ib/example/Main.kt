package ib.example

import io.vertx.core.Vertx.vertx
import io.vertx.core.logging.Log4j2LogDelegateFactory
import io.vertx.core.logging.LoggerFactory

fun main() {
    // Use log4j2 for vert.x logging
    System.setProperty(
        LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME,
        Log4j2LogDelegateFactory::class.qualifiedName!!
    )

    vertx().deployVerticle(Server())
}


package it.plague.blog.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise

class HelloVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>?) {
    val port: String = System.getenv("PORT") ?: "8080"
    vertx
      .createHttpServer()
      .requestHandler { r -> r.response().end("<h1>Hello from my first " + "Vert.x 3 application</h1>") }
      .listen(port.toInt()) { result ->
        if (result.succeeded()) {
          startPromise?.complete()
        } else {
          startPromise?.fail(result.cause())
        }
      }
  }
}

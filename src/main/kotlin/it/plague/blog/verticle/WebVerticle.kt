package it.plague.blog.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.http.HttpMethod
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler

class WebVerticle : AbstractVerticle() {

  private val log = LoggerFactory.getLogger(this.javaClass)
  private val HTTP_PORT = 9000

  override fun start(promise: Promise<Void>?) {
    val router = loadRouter()
    log.info("Try to start WebServer on port $HTTP_PORT")
    getVertx().createHttpServer()
      .requestHandler(router::accept)
      .listen(HTTP_PORT) { accepted ->
        if (accepted.succeeded()) {
          promise?.complete()
          log.info("Successful start WebServer on port $HTTP_PORT")
        } else {
          promise?.fail(accepted.cause())
        }
      }
  }

  private fun loadRouter(): Router {
    val router = Router.router(getVertx())
    router.route(HttpMethod.GET, "/*").handler(StaticHandler.create("web"))

    // Define API REST Routing

    return router
  }
}

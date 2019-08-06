package it.plague.blog.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory


class MainVerticle : AbstractVerticle() {

  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun start(startPromise: Promise<Void>?) {
    val config: JsonObject = config()
    log.info("Start Vert.x Home Blog")
    log.info("with config:\n ${config.encodePrettily()}")

    deploy(HelloVerticle(), config)
  }

  override fun stop(stopPromise: Promise<Void>?) {
    stopPromise?.complete()
  }

  private fun deploy(verticle: AbstractVerticle, config: JsonObject): Promise<Void> {
    val done = Promise.promise<Void>()
    getVertx().deployVerticle(verticle,
      DeploymentOptions().setConfig(config)) { result ->
      if (result.succeeded()) {
        done.complete()
      } else {
        log.error("Error to deploy Verticle: {}", verticle.javaClass.name)
        done.fail(result.cause())
      }
    }
    return done
  }

  /*
  private fun initDb(config: JsonObject): Future<Void> {
      val done = Future.future()
      getVertx().executeBlocking({ initDbFeature ->
          val flyway = Flyway()
          flyway.setDataSource(DataSourceConfig.initDataSource(config))
          flyway.migrate()

          initDbFeature.complete()
      }, { initRes ->
          if (initRes.succeeded()) {
              log.info("Db Init Successfully")
              done.complete()
          } else {
              done.fail(initRes.cause())
          }
      })
      return done
  }
  */
}

package it.plague.blog;

import com.google.inject.Guice;
import com.google.inject.Module;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.VerticleFactory;
import it.plague.blog.config.guice.*;
import it.plague.blog.database.ArticleDatabaseVerticle;
import it.plague.blog.http.HttpVerticle;

import java.util.concurrent.TimeUnit;

public class BlogApplication {

	private static final Logger log = LoggerFactory.getLogger(BlogApplication.class);

	public static void main(String[] args) {
		log.info("Start Vert.x Home Blog");
		Vertx vertx = Vertx.vertx(getOptions());
		VerticleFactory factory = guiceBootstrap(vertx);
		deploy(vertx, factory.prefix(), HttpVerticle.class.getName());
		deploy(vertx, factory.prefix(), ArticleDatabaseVerticle.class.getName());
	}

	private static VerticleFactory guiceBootstrap(Vertx vertx) {
		var injector = Guice.createInjector(getModules(vertx));
		var factory = new GuiceVerticleFactory(injector);
		vertx.registerVerticleFactory(factory);
		return factory;
	}

	private static Module[] getModules(Vertx vertx) {
		return new Module[]{
			new VertxModule(vertx),
			new ConfigModule(),
			new WebModule(vertx),
			new DatabaseModule(vertx)
		};
	}

	private static void deploy(Vertx vertx, String prefix, String verticleName) {
		vertx.deployVerticle(prefix + ":" + verticleName);
	}

	private static VertxOptions getOptions() {
		var eventLoops = 2 * Runtime.getRuntime().availableProcessors();
		return new VertxOptions()
			.setEventLoopPoolSize(eventLoops)
			.setWarningExceptionTime(1)
			.setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(1))
			.setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1))
			.setBlockedThreadCheckInterval(TimeUnit.SECONDS.toMillis(1));
	}
}
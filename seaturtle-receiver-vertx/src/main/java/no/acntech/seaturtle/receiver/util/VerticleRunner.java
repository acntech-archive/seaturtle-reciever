package no.acntech.seaturtle.receiver.util;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public final class VerticleRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerticleRunner.class);

    private VerticleRunner() {
    }

    public static void run(Verticle verticle) {
        run(verticle, null);
    }

    public static void run(Verticle verticle, VertxOptions options) {
        run(verticle, options, null);
    }

    public static void run(Verticle verticle, VertxOptions options, DeploymentOptions deploymentOptions) {
        if (verticle == null) {
            throw new IllegalArgumentException("Passed verticle is null");
        }

        if (options == null) {
            LOGGER.debug("Options are null");
            options = new VertxOptions();
        }

        Consumer<Vertx> runner = vertx -> {
            try {
                if (deploymentOptions != null) {
                    vertx.deployVerticle(verticle, deploymentOptions);
                } else {
                    vertx.deployVerticle(verticle);
                }
            } catch (Throwable t) {
                LOGGER.error("Unable to deploy verticle", t);
            }
        };
        if (options.isClustered()) {
            LOGGER.info("Running in custered mode");
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                    LOGGER.info("Vert.x instance added to cluster");
                } else {
                    LOGGER.error("Unable to add Vert.x instance to cluster", res.cause());
                }
            });
        } else {
            LOGGER.info("Running in standard mode");
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }
}

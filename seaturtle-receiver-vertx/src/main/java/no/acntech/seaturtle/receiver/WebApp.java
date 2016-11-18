package no.acntech.seaturtle.receiver;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import no.acntech.seaturtle.receiver.resource.Resource;
import no.acntech.seaturtle.receiver.util.DefaultVerticle;
import no.acntech.seaturtle.receiver.util.ResourceFactory;
import no.acntech.seaturtle.receiver.util.VerticleRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WebApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebApp.class);

    public static void main(String[] args) {
        List<Resource> resources = ResourceFactory.createResources();
        LOGGER.info("Found {} resources to run", resources.size());
        VerticleRunner.run(new DefaultVerticle(resources), getOptions(), getDeploymentOptions());
    }

    private static VertxOptions getOptions() {
        return new VertxOptions();
    }

    private static DeploymentOptions getDeploymentOptions() {
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setConfig(new JsonObject().put("http.port", 8080).put("context.path", "/seaturtle/api"));
        return deploymentOptions;
    }
}

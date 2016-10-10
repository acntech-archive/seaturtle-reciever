package no.acntech.seaturtle.receiver.util;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import no.acntech.seaturtle.receiver.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVerticle.class);

    private final List<Resource> resources;

    public DefaultVerticle(final List<Resource> resources) {
        if (resources == null || resources.isEmpty()) {
            throw new IllegalArgumentException("SeaTurtle cannot start with no exposed resources");
        }
        this.resources = resources;
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        Router router = Router.router(vertx);

        resources.forEach(r -> {
            LOGGER.info("Registering resource {}", r.getClass().getName());
            router.route(getContextPath("ROUTE", r).concat("*")).handler(BodyHandler.create());
            router.get(getContextPathParam("GET", r)).handler(r::get);
            router.get(getContextPath("GET", r)).handler(r::getAll);
            router.put(getContextPathParam("PUT", r)).handler(r::put);
            router.post(getContextPath("POST", r)).handler(r::post);
            router.delete(getContextPathParam("DELETE", r)).handler(r::delete);
        });

        int port = config().getInteger("http.port", 8080);
        LOGGER.info("Starting Vert.x HTTP server listening on port {}", port);
        vertx.createHttpServer().requestHandler(router::accept).listen(port, result -> {
            if (result.succeeded()) {
                future.complete();
            } else {
                future.fail(result.cause());
            }
        });
    }

    private String getContextPathParam(String action, Resource resource) {
        String contextPath = getContextPathFromResource(resource).concat(":").concat(resource.pathParam());
        LOGGER.debug("Mapping context path {} for {} action on resource {}", contextPath, action, resource.getClass().getName());
        return contextPath;
    }

    private String getContextPath(String action, Resource resource) {
        String contextPath = getContextPathFromResource(resource);
        LOGGER.debug("Mapping context path {} for {} action on resource {}", contextPath, action, resource.getClass().getName());
        return contextPath;
    }

    private String getContextPathFromResource(Resource resource) {
        String resourcePath = sanitizeResourcePath(resource.contextPath());
        return sanitizeContextPath(config().getString("context.path", "/")).concat(resourcePath);
    }

    private String sanitizeContextPath(String contextPath) {
        if (contextPath == null) {
            return "";
        }
        contextPath = contextPath.startsWith("/") ? contextPath : "/".concat(contextPath);
        return contextPath.endsWith("/") ? contextPath : contextPath.concat("/");
    }

    private String sanitizeResourcePath(String resourcePath) {
        if (resourcePath == null) {
            return "";
        }
        resourcePath = resourcePath.startsWith("/") ? resourcePath.replaceFirst("^/", "") : resourcePath;
        return resourcePath.endsWith("/") ? resourcePath.replaceFirst("/$", "") : resourcePath;
    }
}

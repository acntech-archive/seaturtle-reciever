package no.acntech.seaturtle.receiver.resource;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import no.acntech.seaturtle.receiver.domain.hateoas.HateoasRoot;
import no.acntech.seaturtle.receiver.domain.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static no.acntech.seaturtle.receiver.util.Header.APPLICATION_JSON;
import static no.acntech.seaturtle.receiver.util.Header.CONTENT_TYPE;
import static no.acntech.seaturtle.receiver.util.Header.TEXT_PLAIN;

public abstract class AbstractResource implements Resource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String contextPath() {
        return this.getClass().getSimpleName().replace("Resource", "").toLowerCase();
    }

    @Override
    public String pathParam() {
        return "id";
    }

    @Override
    public void get(RoutingContext routingContext) {
        logger.debug("Invoking default GET action");
        sendHttp501(routingContext);
    }

    @Override
    public void getAll(RoutingContext routingContext) {
        logger.debug("Invoking default GET all action");
        sendHttp501(routingContext);
    }

    @Override
    public void put(RoutingContext routingContext) {
        logger.debug("Invoking default PUT action");
        sendHttp501(routingContext);
    }

    @Override
    public void post(RoutingContext routingContext) {
        logger.debug("Invoking default POST action");
        sendHttp501(routingContext);
    }

    @Override
    public void delete(RoutingContext routingContext) {
        logger.debug("Invoking default DELETE action");
        sendHttp501(routingContext);
    }

    protected void sendHttp501(RoutingContext routingContext) {
        int statusCode = 501;
        logHTTPMethod(routingContext, statusCode);
        String responseText = "501 Not Implemented";
        routingContext.response().setStatusCode(statusCode);
        sendText(routingContext, responseText);
    }

    protected void sendHttp201(RoutingContext routingContext, Object json) {
        int statusCode = 201;
        logHTTPMethod(routingContext, statusCode);
        routingContext.response().setStatusCode(statusCode);
        sendHateoasJson(routingContext, json);
    }

    protected <T> T readJson(RoutingContext routingContext, Class<T> clazz) {
        String requestBody = routingContext.getBodyAsString();
        logger.trace("Received message {}", requestBody);
        return Json.decodeValue(requestBody, clazz);
    }

    protected void sendJson(RoutingContext routingContext, Object json) {
        routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(Json.encodePrettily(json));
    }

    protected void sendHateoasJson(RoutingContext routingContext, Object json) {
        String uri = routingContext.request().absoluteURI();
        List<Link> links = new ArrayList<>();
        links.add(new Link("self", uri));
        HateoasRoot root = new HateoasRoot(json, links);
        routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(Json.encodePrettily(root));
    }

    protected void sendText(RoutingContext routingContext, Object text) {
        routingContext.response().putHeader(CONTENT_TYPE, TEXT_PLAIN).end(text.toString());
    }

    private void logHTTPMethod(RoutingContext routingContext, int statusCode) {
        String method = routingContext.request().method().name();
        String uri = routingContext.request().absoluteURI();
        logger.debug("Responding with HTTP {} for a {} request against {}", statusCode, method, uri);
    }
}

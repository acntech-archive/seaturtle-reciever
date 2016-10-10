package no.acntech.seaturtle.receiver.resource;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        sendText(routingContext.response().setStatusCode(statusCode), responseText);
    }

    protected void sendHttp201(RoutingContext routingContext, Object json) {
        int statusCode = 201;
        logHTTPMethod(routingContext, statusCode);
        sendJson(routingContext.response().setStatusCode(statusCode), json);
    }

    protected <T> T readJson(RoutingContext routingContext, Class<T> clazz) {
        String requestBody = routingContext.getBodyAsString();
        logger.trace("Received message {}", requestBody);
        return Json.decodeValue(requestBody, clazz);
    }

    protected void sendJson(HttpServerResponse response, Object json) {
        response.putHeader(CONTENT_TYPE, APPLICATION_JSON).end(Json.encodePrettily(json));
    }

    protected void sendText(HttpServerResponse response, Object text) {
        response.putHeader(CONTENT_TYPE, TEXT_PLAIN).end(text.toString());
    }

    private void logHTTPMethod(RoutingContext routingContext, int statusCode) {
        String method = routingContext.request().method().name();
        String uri = routingContext.request().absoluteURI();
        logger.debug("Responding with HTTP {} for a {} request against {}", statusCode, method, uri);
    }
}

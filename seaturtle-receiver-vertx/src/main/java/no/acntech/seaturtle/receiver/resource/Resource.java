package no.acntech.seaturtle.receiver.resource;

import io.vertx.ext.web.RoutingContext;

public interface Resource {

    String contextPath();

    String pathParam();

    void get(RoutingContext routingContext);

    void getAll(RoutingContext routingContext);

    void put(RoutingContext routingContext);

    void post(RoutingContext routingContext);

    void delete(RoutingContext routingContext);
}

package no.acntech.seaturtle.receiver.resource;

import io.vertx.ext.web.RoutingContext;
import no.acntech.seaturtle.receiver.domain.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingResource.class);

    @Override
    public void getAll(RoutingContext routingContext) {
        LOGGER.debug("Ping received");
        sendJson(routingContext, new Ping());
    }
}

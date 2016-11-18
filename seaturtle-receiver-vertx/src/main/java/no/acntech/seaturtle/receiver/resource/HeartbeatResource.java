package no.acntech.seaturtle.receiver.resource;

import io.vertx.ext.web.RoutingContext;
import no.acntech.seaturtle.receiver.domain.Heartbeat;
import no.acntech.seaturtle.receiver.storage.HeartbeatMessageBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HeartbeatResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatResource.class);
    private static final HeartbeatMessageBuffer MESSAGE_BUFFER = new HeartbeatMessageBuffer();

    @Override
    public void getAll(RoutingContext routingContext) {
        LOGGER.debug("Getting heartbeats");
        List<Heartbeat> heartbeats = MESSAGE_BUFFER.list();
        sendJson(routingContext, heartbeats);
    }

    @Override
    public void post(RoutingContext routingContext) {
        final Heartbeat heartbeat = readJson(routingContext, Heartbeat.class);
        MESSAGE_BUFFER.put(heartbeat);
        sendHttp201(routingContext, heartbeat);
    }
}

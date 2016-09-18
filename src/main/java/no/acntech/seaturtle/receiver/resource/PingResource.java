package no.acntech.seaturtle.receiver.resource;

import no.acntech.seaturtle.receiver.message.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
public class PingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingResource.class);

    @GET
    public Ping ping() {
        LOGGER.debug("Ping received");
        return new Ping();
    }
}

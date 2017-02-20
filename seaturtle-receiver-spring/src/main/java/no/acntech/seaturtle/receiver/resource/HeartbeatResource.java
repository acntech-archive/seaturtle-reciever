package no.acntech.seaturtle.receiver.resource;

import no.acntech.seaturtle.receiver.domain.Heartbeat;
import no.acntech.seaturtle.receiver.storage.HeartbeatMessageBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping(value = "/heartbeat", produces = {APPLICATION_JSON_UTF8_VALUE}, consumes = {APPLICATION_JSON_UTF8_VALUE})
@RestController
public class HeartbeatResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatResource.class);

    private final HeartbeatMessageBuffer messageBuffer;

    @Autowired
    public HeartbeatResource(final HeartbeatMessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    @RequestMapping(method = GET)
    public List<Heartbeat> getAll() {
        LOGGER.info("Retrieving heartbeat events");
        return messageBuffer.list();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Heartbeat post(@RequestBody @Valid final Heartbeat heartbeat) throws InterruptedException {
        LOGGER.info("Heartbeat event received: {}", heartbeat);
        messageBuffer.put(heartbeat);
        return heartbeat;
    }
}

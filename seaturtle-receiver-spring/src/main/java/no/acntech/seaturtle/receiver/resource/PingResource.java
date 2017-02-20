package no.acntech.seaturtle.receiver.resource;

import no.acntech.seaturtle.receiver.domain.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping(value = "/ping", produces = {APPLICATION_JSON_UTF8_VALUE}, consumes = {APPLICATION_JSON_UTF8_VALUE})
@RestController
public class PingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingResource.class);

    @RequestMapping(method = GET)
    public Ping get() {
        LOGGER.debug("Ping received");
        return new Ping();
    }
}

package no.acntech.seaturtle.receiver;

import no.acntech.seaturtle.receiver.message.Heartbeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

//@ApplicationScoped
public class TestBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBean.class);

    @PostConstruct
    public void onInit() {
        LOGGER.info("It's alive!");
    }

    public List<Heartbeat> list() {
        LOGGER.info("List!");
        return new ArrayList<>();
    }
}

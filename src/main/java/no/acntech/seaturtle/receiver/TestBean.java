package no.acntech.seaturtle.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@ApplicationScoped
public class TestBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBean.class);

    //@PostConstruct
    public void onInit() {
        LOGGER.info("It's alive!");
    }
}

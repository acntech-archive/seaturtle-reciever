package no.acntech.seaturtle.receiver.kafka;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class KafkaClient {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Properties readProperties(String propertiesFileName) {
        try (InputStream propertiesFile = Resources.getResource(propertiesFileName).openStream()) {
            Properties properties = new Properties();
            properties.load(propertiesFile);
            return properties;
        } catch (IOException e) {
            throw new KafkaException("Unable to read kafka properties", e);
        }
    }

    protected Map<String, String> readConfig(String propertiesFileName) {
        return readProperties(propertiesFileName)
                .entrySet()
                .stream()
                .collect(Collectors
                        .toMap(e -> String.valueOf(e.getKey()), e -> String.valueOf(e.getValue())));
    }
}
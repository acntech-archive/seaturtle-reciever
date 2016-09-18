package no.acntech.seaturtle.receiver.message;

import java.time.ZonedDateTime;

public class Ping {

    private ZonedDateTime timestamp;

    public Ping() {
        timestamp = ZonedDateTime.now();
    }

    public String getTimestamp() {
        return timestamp.toString();
    }
}

package no.acntech.seaturtle.receiver.message;

import java.time.ZonedDateTime;

public class Heartbeat {

    private ZonedDateTime timestamp;
    private String remote;
    private String event;

    public Heartbeat() {
        this.timestamp = ZonedDateTime.now();
    }

    public Heartbeat(String event) {
        this.timestamp = ZonedDateTime.now();
        this.remote = "N/A";
        this.event = event;
    }

    public Heartbeat(String remote, String event) {
        this.timestamp = ZonedDateTime.now();
        this.remote = remote;
        this.event = event;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}

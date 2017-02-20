package no.acntech.seaturtle.receiver.storage;


import no.acntech.seaturtle.receiver.domain.Heartbeat;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatMessageBuffer extends MessageBuffer<Heartbeat> {
}

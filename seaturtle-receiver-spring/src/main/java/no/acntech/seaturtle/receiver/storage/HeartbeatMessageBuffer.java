package no.acntech.seaturtle.receiver.storage;

import no.acntech.seaturtle.receiver.message.Heartbeat;
import org.springframework.stereotype.Repository;

@Repository
public class HeartbeatMessageBuffer extends MessageBuffer<Heartbeat> {
}

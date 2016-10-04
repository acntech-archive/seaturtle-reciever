package no.acntech.seaturtle.receiver.storage;

import no.acntech.seaturtle.receiver.message.Heartbeat;

import javax.inject.Named;

@Named
public class HeartbeatMessageBuffer extends MessageBuffer<Heartbeat> {
}

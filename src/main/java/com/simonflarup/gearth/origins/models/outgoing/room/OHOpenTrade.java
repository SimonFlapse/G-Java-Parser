package com.simonflarup.gearth.origins.models.outgoing.room;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHOpenTrade {
    private final int targetId;

    public OHOpenTrade(ShockPacketOutgoing packet) {
        String rawMessage = packet.toString();
        rawMessage = rawMessage.substring(2);
        this.targetId = Integer.parseInt(rawMessage);
    }

    public OHOpenTrade(int targetId) {
        this.targetId = targetId;
    }
}

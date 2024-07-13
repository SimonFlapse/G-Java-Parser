package com.simonflarup.gearth.origins.models.outgoing.room;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHAddTradeItem {
    private final int itemId;

    public OHAddTradeItem(ShockPacketOutgoing packet) {
        String rawMessage = packet.toString();
        rawMessage = rawMessage.substring(2);
        this.itemId = Integer.parseInt(rawMessage);
    }

    public OHAddTradeItem(int itemId) {
        this.itemId = itemId;
    }
}

package com.simonflarup.gearth.origins.models.incoming.room;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHTradeItems {
    private final String initiator;
    private final boolean initiatorAccepted;
    private final String target;
    private final boolean targetAccepted;
    private final OHStripInfo[] initiatorTradeItems;
    private final OHStripInfo[] targetTradeItems;

    public OHTradeItems(ShockPacketIncoming packet) {
        this.initiator = packet.readString();
        this.initiatorAccepted = packet.readBoolean();
        this.initiatorTradeItems = OHStripInfo.getOHStripInfoArray(packet);
        this.target = packet.readString();
        this.targetAccepted = packet.readBoolean();
        this.targetTradeItems = OHStripInfo.getOHStripInfoArray(packet);
    }
}

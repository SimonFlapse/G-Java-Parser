package com.simonflarup.gearth.origins.models.outgoing.room;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import com.simonflarup.gearth.origins.utils.ExpressionBuilder;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;

public class OHAddStripItem implements OHServerPacket {

    private final int itemId;
    private final OHStripItemType type;

    public OHAddStripItem(int itemId, OHStripItemType type) {
        this.itemId = itemId;
        this.type = type;
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket() {
        String type = this.type.name().toLowerCase();
        ExpressionBuilder builder = ExpressionBuilder.start(HMessage.Direction.TOSERVER, "ADDSTRIPITEM")
                .raw("new")
                .space()
                .raw(type)
                .space()
                .raw(String.valueOf(itemId));
        return new ShockPacketOutgoing(builder.build());
    }
}

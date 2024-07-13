package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.outgoing.room.OHStripItemType;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class OHStripItem extends OHStripInfo {
    private final String posterId;

    public OHStripItem(ShockPacketIncoming packet) {
        super(packet);
        this.posterId = packet.readString();
    }

    public OHStripItem(int id, int position, int itemId, String className, String posterId) {
        super(id, position, OHStripItemType.ITEM, itemId, className);
        this.posterId = posterId;
    }

    public String getFullClassName() {
        return String.format("%s_%s", this.getClassName(), posterId);
    }

    @Override
    protected void appendToPacket(ShockPacketIncoming packet) {
        super.appendToPacket(packet);
        packet.appendString(this.posterId);
    }
}

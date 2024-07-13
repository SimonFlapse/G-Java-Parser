package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.outgoing.room.OHStripItemType;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class OHStripStuff extends OHStripInfo {
    private final int width;
    private final int height;
    private final String colors;

    public OHStripStuff(ShockPacketIncoming packet) {
        super(packet);
        this.width = packet.readInteger();
        this.height = packet.readInteger();
        this.colors = packet.readString();
    }

    public OHStripStuff(int id, int position, int itemId, String className, int width, int height, String colors) {
        super(id, position, OHStripItemType.STUFF, itemId, className);
        this.width = width;
        this.height = height;
        this.colors = colors;
    }

    @Override
    protected void appendToPacket(ShockPacketIncoming packet) {
        super.appendToPacket(packet);
        packet.appendInt(this.width);
        packet.appendInt(this.height);
        packet.appendString(this.colors);
    }
}

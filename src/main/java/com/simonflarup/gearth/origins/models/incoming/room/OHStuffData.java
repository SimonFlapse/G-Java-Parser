package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHStuffData implements OHClientPacket {
    private final int targetId;
    private final String stuffData;

    public OHStuffData(OHMessageIn message) {
        ShockPacketIncoming packet = message.getPacket();
        this.targetId = Integer.parseInt(packet.readString());
        this.stuffData = packet.readString();
    }

    public OHStuffData(int targetId, String stuffData) {
        this.targetId = targetId;
        this.stuffData = stuffData;
    }

    @Override
    public ShockPacketIncoming getIncomingPacket(PacketInfoManager packetInfoManager) {
        ShockPacketIncoming packet = new ShockPacketIncoming("STUFFDATAUPDATE");
        packet.appendString(String.valueOf(this.targetId));
        packet.appendString(this.stuffData);
        return packet;
    }
}

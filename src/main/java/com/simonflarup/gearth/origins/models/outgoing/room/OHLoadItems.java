package com.simonflarup.gearth.origins.models.outgoing.room;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHLoadItems implements OHServerPacket {
    private final OHRoomItemType type;

    public OHLoadItems(OHRoomItemType type) {
        this.type = type;
    }

    public OHLoadItems(ShockPacketOutgoing packet, PacketInfoManager packetInfoManager) {
        PacketInfo info = packetInfoManager.getPacketInfoFromHeaderId(HMessage.Direction.TOSERVER, packet.headerId());
        this.type = info.getName().equals("G_OBJS") ? OHRoomItemType.FLOOR : OHRoomItemType.WALL;
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
        String headerName = type == OHRoomItemType.FLOOR ? "G_OBJS" : "G_ITEMS";
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, headerName);
        return new ShockPacketOutgoing(packetInfo.getHeaderId());
    }
}

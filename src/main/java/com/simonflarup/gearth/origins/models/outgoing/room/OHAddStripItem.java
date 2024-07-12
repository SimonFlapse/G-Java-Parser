package com.simonflarup.gearth.origins.models.outgoing.room;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacket;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHAddStripItem implements OHServerPacket {

    private final int itemId;
    private final OHStripItemType type;

    public OHAddStripItem(ShockPacket packet) {
        // The message is missing the [2] byte which causes packet.readString() to throw an index out of bound exception
        String message = packet.toString();
        String[] messages = message.split(" ");
        this.itemId = Integer.parseInt(messages[2]);
        this.type = OHStripItemType.valueOf(messages[1].toUpperCase());
    }

    public OHAddStripItem(int itemId, OHStripItemType type) {
        this.itemId = itemId;
        this.type = type;
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
        String type = this.type.name().toLowerCase();
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, "ADDSTRIPITEM");
        ShockPacketOutgoing packet = new ShockPacketOutgoing(packetInfo.getHeaderId());
        packet.appendString(String.format("new %s %d", type, itemId));
        return packet;
    }
}

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
public class OHAddTradeItem implements OHServerPacket {
    private final int itemId;

    public OHAddTradeItem(ShockPacketOutgoing packet) {
        String rawMessage = packet.toString();
        rawMessage = rawMessage.substring(2);
        this.itemId = Integer.parseInt(rawMessage);
    }

    public OHAddTradeItem(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, "TRADE_ADDITEM");
        ShockPacketOutgoing packet = new ShockPacketOutgoing(packetInfo.getHeaderId());
        packet.appendBytes(String.valueOf(this.itemId).getBytes());
        return packet;
    }
}

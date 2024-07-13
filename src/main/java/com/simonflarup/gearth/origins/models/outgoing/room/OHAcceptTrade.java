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
public class OHAcceptTrade implements OHServerPacket {
    private final OHAcceptTradeType type;

    public OHAcceptTrade(ShockPacketOutgoing packet, PacketInfoManager packetInfoManager) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromHeaderId(HMessage.Direction.TOSERVER, packet.headerId());

        this.type = packetInfo.getName().equals("TRADE_ACCEPT") ? OHAcceptTradeType.ACCEPT : OHAcceptTradeType.UNACCEPT;
    }

    public OHAcceptTrade(OHAcceptTradeType type) {
        this.type = type;
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, this.type == OHAcceptTradeType.ACCEPT ? "TRADE_ACCEPT" : "TRADE_UNACCEPT");
        return new ShockPacketOutgoing(packetInfo.getHeaderId());
    }

    public enum OHAcceptTradeType {
        ACCEPT,
        UNACCEPT;
    }
}

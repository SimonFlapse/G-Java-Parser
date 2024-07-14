package com.simonflarup.gearth.origins.models.incoming.chat;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacket;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHChatIn implements OHClientPacket {
    private final int speakerId;
    private final String message;
    private final OHChatInType type;

    public OHChatIn(ShockPacket packet, PacketInfoManager packetInfoManager) {
        PacketInfo info = packetInfoManager.getPacketInfoFromHeaderId(HMessage.Direction.TOCLIENT, packet.headerId());
        this.type = OHChatInType.fromName(info.getName());
        this.speakerId = packet.readInteger();
        this.message = packet.readString();
    }

    public OHChatIn(int speakerId, String message, OHChatInType type) {
        this.speakerId = speakerId;
        this.message = message;
        this.type = type;
    }

    @Override
    public ShockPacketIncoming getIncomingPacket(PacketInfoManager packetInfoManager) {
        String headerName = this.type.getIncomingHeaderName();
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, headerName);
        ShockPacketIncoming packet = new ShockPacketIncoming(packetInfo.getHeaderId());
        packet.appendInt(this.speakerId);
        ShockPacketUtils.appendRawMessage(this.message, packet);
        packet.appendByte((byte) 2);
        return packet;
    }
}

package com.simonflarup.gearth.origins.models.outgoing.chat;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacket;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;

@Getter
@ToString
public class OHChatOut implements OHServerPacket {
    private final String message;
    private final OHChatOutType type;
    private final String targetUser;

    public OHChatOut(ShockPacket packet, PacketInfoManager packetInfoManager) {
        PacketInfo info = packetInfoManager.getPacketInfoFromHeaderId(HMessage.Direction.TOSERVER, packet.headerId());
        this.type = OHChatOutType.fromName(info.getName());

        String message = packet.readString(StandardCharsets.ISO_8859_1);
        String targetUser = "";

        if (type == OHChatOutType.WHISPER) {
            String[] split = message.split(" ", 2);
            targetUser = split[0];
            targetUser = targetUser != null ? targetUser : "";
            message = split[1];
        }

        this.message = message;
        this.targetUser = targetUser;
    }

    public OHChatOut(String message, OHChatOutType type) {
        this(message, type, "");
    }

    public OHChatOut(String message, OHChatOutType type, String targetUser) {
        this.message = message;
        this.type = type;
        this.targetUser = targetUser;
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
        String headerName = this.type.getOutgoingHeaderName();
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, headerName);
        ShockPacketOutgoing packet = new ShockPacketOutgoing(packetInfo.getHeaderId());

        String message = this.message;

        if (this.type == OHChatOutType.WHISPER) {
            message = String.format("%s %s", this.targetUser != null ? this.targetUser : " ", message);
        }

        packet.appendString(message);
        return packet;
    }
}

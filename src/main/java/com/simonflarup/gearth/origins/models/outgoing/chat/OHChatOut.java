package com.simonflarup.gearth.origins.models.outgoing.chat;

import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacket;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;

@Getter
@ToString
public class OHChatOut {
    private final String message;
    private final OHChatOutType type;

    public OHChatOut(ShockPacket packet, PacketInfoManager packetInfoManager) {
        PacketInfo info = packetInfoManager.getPacketInfoFromHeaderId(HMessage.Direction.TOSERVER, packet.headerId());
        this.type = OHChatOutType.fromName(info.getName());

        String message = packet.readString(StandardCharsets.ISO_8859_1);

        if (type == OHChatOutType.WHISPER) {
            message = message.substring(1);
        }

        this.message = message;
    }
}

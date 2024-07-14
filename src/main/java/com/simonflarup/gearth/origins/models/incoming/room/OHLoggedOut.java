package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHLoggedOut implements OHClientPacket {
    private final int userRoomId;

    public OHLoggedOut(ShockPacketIncoming packet) {
        String rawMessage = ShockPacketUtils.getRawMessage(packet);
        this.userRoomId = Integer.parseInt(rawMessage);
    }

    public OHLoggedOut(int userRoomId) {
        this.userRoomId = userRoomId;
    }

    @Override
    public ShockPacketIncoming getIncomingPacket(PacketInfoManager packetInfoManager) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "LOGOUT");
        ShockPacketIncoming packet = new ShockPacketIncoming(packetInfo.getHeaderId());
        ShockPacketUtils.appendRawMessage(String.valueOf(this.userRoomId), packet);
        return packet;
    }
}

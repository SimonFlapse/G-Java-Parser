package com.simonflarup.gearth.origins.models.outgoing.room;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHOpenTrade implements OHServerPacket {
    private final int targetId;

    public OHOpenTrade(ShockPacketOutgoing packet) {
        String rawMessage = ShockPacketUtils.getRawMessage(packet);
        this.targetId = Integer.parseInt(rawMessage);
    }

    public OHOpenTrade(int targetId) {
        this.targetId = targetId;
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, "TRADE_OPEN");
        ShockPacketOutgoing packet = new ShockPacketOutgoing(packetInfo.getHeaderId());
        ShockPacketUtils.appendRawMessage(String.valueOf(this.targetId), packet);
        return packet;
    }
}

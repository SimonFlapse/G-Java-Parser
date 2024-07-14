package com.simonflarup.gearth.origins.models.outgoing.registrat;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;

public class OHInfoRetrieve implements OHServerPacket {
    public OHInfoRetrieve() {
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, "INFORETRIEVE");
        return new ShockPacketOutgoing(packetInfo.getHeaderId());
    }
}

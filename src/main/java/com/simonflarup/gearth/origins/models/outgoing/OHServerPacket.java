package com.simonflarup.gearth.origins.models.outgoing;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import gearth.services.packet_info.PacketInfoManager;

public interface OHServerPacket {
    ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager);
}

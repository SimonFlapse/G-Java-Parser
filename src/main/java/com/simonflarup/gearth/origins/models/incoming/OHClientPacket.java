package com.simonflarup.gearth.origins.models.incoming;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfoManager;

public interface OHClientPacket {
    ShockPacketIncoming getIncomingPacket(PacketInfoManager packetInfoManager);
}

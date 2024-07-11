package com.simonflarup.gearth.origins.models.outgoing;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;

public interface OHServerPacket {
    ShockPacketOutgoing getOutgoingPacket();
}

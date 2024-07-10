package com.simonflarup.gearth.origins.internal.events;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

public interface IncomingPacketHandler {
    void handlePacket(ShockPacketIncoming packet);
}

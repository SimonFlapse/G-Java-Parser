package com.simonflarup.gearth.origins.events;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

public interface IncomingPacketHandler {
    void handlePacket(ShockPacketIncoming packet);
}

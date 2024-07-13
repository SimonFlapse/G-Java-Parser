package com.simonflarup.gearth.origins.internal.packets;

public interface OutgoingPacketHandler {
    void handlePacket(OHMessageOut message);
}

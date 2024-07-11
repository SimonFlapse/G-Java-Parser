package com.simonflarup.gearth.origins.services;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;

public interface OHPacketSender {
    boolean toServer(OHServerPacket packet);
}

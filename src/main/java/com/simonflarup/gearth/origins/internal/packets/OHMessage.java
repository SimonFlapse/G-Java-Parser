package com.simonflarup.gearth.origins.internal.packets;

import com.simonflarup.gearth.origins.internal.OHContext;
import gearth.protocol.HMessage;

public interface OHMessage {
    OHContext getContext();

    boolean isBlocked();

    void setBlocked(boolean blocked);

    HMessage.Direction getDestination();

    boolean isCorrupted();
}

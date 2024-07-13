package com.simonflarup.gearth.origins.internal;

import com.simonflarup.gearth.origins.OHExtension;
import com.simonflarup.gearth.origins.internal.events.EventSystem;
import gearth.services.packet_info.PacketInfoManager;

public interface OHContext {
    OHExtension getExtension();

    EventSystem getEventSystem();

    PacketInfoManager getPacketInfoManager();
}

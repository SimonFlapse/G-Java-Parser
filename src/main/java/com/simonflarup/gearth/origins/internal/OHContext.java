package com.simonflarup.gearth.origins.internal;

import com.simonflarup.gearth.origins.internal.events.EventSystem;
import gearth.extensions.ExtensionBase;
import gearth.services.packet_info.PacketInfoManager;

public interface OHContext {
    ExtensionBase getExtension();

    EventSystem getEventSystem();

    PacketInfoManager getPacketInfoManager();
}

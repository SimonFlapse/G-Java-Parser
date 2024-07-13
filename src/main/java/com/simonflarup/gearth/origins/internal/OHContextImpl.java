package com.simonflarup.gearth.origins.internal;

import com.simonflarup.gearth.origins.InternalExtensionProvider;
import com.simonflarup.gearth.origins.OHExtension;
import com.simonflarup.gearth.origins.internal.events.EventSystem;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;

@Getter
public class OHContextImpl implements OHContext {
    private final OHExtension extension;
    private final EventSystem eventSystem;

    public OHContextImpl(InternalExtensionProvider extensionProvider, EventSystem eventSystem) {
        this.extension = extensionProvider.getExtension();
        this.eventSystem = eventSystem;
    }

    public PacketInfoManager getPacketInfoManager() {
        return extension.getPacketInfoManager();
    }
}

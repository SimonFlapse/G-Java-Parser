package com.simonflarup.gearth.origins.internal.services;

import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.services.OHFlatManager;
import com.simonflarup.gearth.origins.services.OHPacketSender;
import com.simonflarup.gearth.origins.services.OHServiceProvider;
import lombok.Getter;

@Getter
public class OHServiceProviderImpl implements OHServiceProvider {
    private final OHFlatManager flatManager;
    private final OHPacketSender packetSender;

    public OHServiceProviderImpl(OHContext context) {
        this.flatManager = OHFlatManagerImpl.getInstance(context.getEventSystem());
        this.packetSender = new OHPacketSenderImpl(context.getExtension());
    }
}

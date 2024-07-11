package com.simonflarup.gearth.origins.services;

public interface OHServiceProvider {
    OHFlatManager getFlatManager();

    OHPacketSender getPacketSender();
}

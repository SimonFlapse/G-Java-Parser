package com.simonflarup.gearth.origins.internal.services;

import com.simonflarup.gearth.origins.internal.events.EventSystem;
import com.simonflarup.gearth.origins.services.OHFlatManager;
import com.simonflarup.gearth.origins.services.OHServiceProvider;

public class OHServiceProviderImpl implements OHServiceProvider {
    private final OHFlatManager ohFlatManager;

    public OHServiceProviderImpl(EventSystem eventSystem) {
        this.ohFlatManager = OHFlatManagerImpl.getInstance(eventSystem);
    }

    @Override
    public OHFlatManager getFlatManager() {
        return ohFlatManager;
    }
}

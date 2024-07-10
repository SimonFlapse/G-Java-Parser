package com.simonflarup.gearth.origins.services.internal;

import com.simonflarup.gearth.origins.services.OHFlatManager;
import com.simonflarup.gearth.origins.services.OHServiceProvider;

public class OHServiceProviderImpl implements OHServiceProvider {
    private final OHFlatManager ohFlatManager;

    public OHServiceProviderImpl() {
        this.ohFlatManager = OHFlatManagerImpl.getInstance();
    }

    @Override
    public OHFlatManager getFlatManager() {
        return ohFlatManager;
    }
}

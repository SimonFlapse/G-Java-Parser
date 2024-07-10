package com.simonflarup.gearth.origins;

import com.simonflarup.gearth.origins.internal.Facade;
import com.simonflarup.gearth.origins.services.OHServiceProvider;
import gearth.extensions.Extension;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class OHExtension extends Extension {
    private final Facade facade;

    protected OHExtension(String[] args) {
        super(args);
        this.facade = Facade.getInstance(new InternalExtensionProvider(this));
    }

    public OHServiceProvider getServiceProvider() {
        return facade.getServiceProvider();
    }

    @Override
    protected void initExtension() {
        onConnect((host, port, APIVersion, versionClient, client) -> {
            if (!Objects.equals(versionClient, "SHOCKWAVE")) {
                log.error("This extension only works with Habbo Hotel: Origins");
                System.exit(0);
            }
        });

        facade.setupInterceptors();
    }
}

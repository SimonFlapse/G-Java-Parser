package com.simonflarup.gearth.origins.internal;

import com.simonflarup.gearth.origins.InternalExtensionProvider;
import com.simonflarup.gearth.origins.internal.events.EventSystem;
import com.simonflarup.gearth.origins.internal.intercepts.OHInterceptor;
import com.simonflarup.gearth.origins.internal.services.OHServiceProviderImpl;
import com.simonflarup.gearth.origins.services.OHServiceProvider;
import lombok.Getter;

public class Facade {
    private static Facade INSTANCE;

    @Getter
    private final EventSystem eventSystem;
    @Getter
    private final OHServiceProvider serviceProvider;
    private final OHInterceptor interceptor;

    private boolean interceptorsInitialized = false;

    private Facade(EventSystem eventSystem, OHServiceProvider serviceProvider, OHInterceptor interceptor) {
        this.eventSystem = eventSystem;
        this.serviceProvider = serviceProvider;
        this.interceptor = interceptor;
    }

    public static Facade getInstance(InternalExtensionProvider extensionProvider) {
        if (INSTANCE == null) {
            EventSystem eventSystem = new EventSystem(extensionProvider);
            INSTANCE = new Facade(eventSystem, new OHServiceProviderImpl(eventSystem), new OHInterceptor(eventSystem, extensionProvider));
        }
        return INSTANCE;
    }

    public void setupInterceptors() {
        if (interceptorsInitialized) {
            throw new IllegalStateException("Interceptors already initialized");
        }
        interceptorsInitialized = true;
        interceptor.setupInterceptors();
    }
}

package com.simonflarup.gearth.origins.internal.events;

public interface EventSubscriber {
    void register(Object object);

    void registerPriority(Object object);
}

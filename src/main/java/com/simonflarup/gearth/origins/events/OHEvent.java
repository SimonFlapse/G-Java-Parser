package com.simonflarup.gearth.origins.events;

public interface OHEvent<T> {
    T get();

    void silenceMessage();
}

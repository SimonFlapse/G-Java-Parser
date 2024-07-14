package com.simonflarup.gearth.origins.events;

public interface OHEvent<T> {
    T get();

    void silenceMessage();

    boolean isSilenced();

    boolean isCorrupted();

    Destination getDestination();

    enum Destination {
        CLIENT,
        SERVER
    }
}

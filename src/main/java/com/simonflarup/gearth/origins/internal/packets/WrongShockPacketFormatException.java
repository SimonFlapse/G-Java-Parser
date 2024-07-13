package com.simonflarup.gearth.origins.internal.packets;

public class WrongShockPacketFormatException extends IllegalArgumentException {
    public WrongShockPacketFormatException(String message) {
        super(message);
    }
}

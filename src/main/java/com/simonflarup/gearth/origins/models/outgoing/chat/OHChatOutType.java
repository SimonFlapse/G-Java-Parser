package com.simonflarup.gearth.origins.models.outgoing.chat;

public enum OHChatOutType {
    SAY,
    WHISPER,
    SHOUT;

    public static OHChatOutType fromName(String name) {
        if (name.equalsIgnoreCase("CHAT")) {
            return SAY;
        }

        return OHChatOutType.valueOf(name.toUpperCase());
    }
}

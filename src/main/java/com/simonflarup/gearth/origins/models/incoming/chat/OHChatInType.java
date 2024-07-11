package com.simonflarup.gearth.origins.models.incoming.chat;

import lombok.Getter;

@Getter
public enum OHChatInType {
    SAY("CHAT"),
    WHISPER("CHAT_2"),
    SHOUT("CHAT_3");

    private final String incomingHeaderName;

    OHChatInType(String incomingHeaderName) {
        this.incomingHeaderName = incomingHeaderName;
    }

    public static OHChatInType fromName(String name) {
        for (OHChatInType type : values()) {
            if (type.incomingHeaderName.equals(name)) {
                return type;
            }
        }
        return SAY;
    }
}

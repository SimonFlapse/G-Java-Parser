package com.simonflarup.gearth.origins.models.outgoing.chat;

import lombok.Getter;

@Getter
public enum OHChatOutType {
    SAY("CHAT"),
    WHISPER("WHISPER"),
    SHOUT("SHOUT");

    private final String outgoingHeaderName;

    OHChatOutType(String outgoingHeaderName) {
        this.outgoingHeaderName = outgoingHeaderName;
    }

    public static OHChatOutType fromName(String name) {
        for (OHChatOutType type : values()) {
            if (type.outgoingHeaderName.equals(name)) {
                return type;
            }
        }
        return SAY;
    }
}

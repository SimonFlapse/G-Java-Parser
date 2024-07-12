package com.simonflarup.gearth.origins.events.chat;

import com.simonflarup.gearth.origins.models.outgoing.chat.OHChatOut;

public interface OnChatOutEvent {

    OHChatOut getChatOut();
    void silenceMessage();
}

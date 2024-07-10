package com.simonflarup.gearth.origins.events.chat;

import com.simonflarup.gearth.origins.models.outgoing.chat.OHChatOut;

public interface OnChatEvent {

    OHChatOut getChatOut();
    void silenceMessage();
}

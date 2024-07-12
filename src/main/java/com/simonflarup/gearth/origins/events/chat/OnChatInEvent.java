package com.simonflarup.gearth.origins.events.chat;

import com.simonflarup.gearth.origins.models.incoming.chat.OHChatIn;

public interface OnChatInEvent {

    OHChatIn getChatIn();

    void silenceMessage();
}

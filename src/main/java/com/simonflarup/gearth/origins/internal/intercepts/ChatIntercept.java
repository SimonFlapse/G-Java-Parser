package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.chat.OnChatEvent;
import com.simonflarup.gearth.origins.internal.events.EventPublisher;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;

import java.nio.charset.StandardCharsets;

class ChatIntercept extends AbstractIntercept {

    static void onChat(HMessage hMessage, EventPublisher eventPublisher) {
        ShockPacketOutgoing packet = ShockPacketUtils.getShockPacketOutgoingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        String message = packet.readString(StandardCharsets.ISO_8859_1);
        eventPublisher.post(new OnChatEvent() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public void silenceMessage() {
                hMessage.setBlocked(true);
            }
        });
    }
}

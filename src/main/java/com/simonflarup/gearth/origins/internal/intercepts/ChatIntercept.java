package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.chat.OnChatInEvent;
import com.simonflarup.gearth.origins.events.chat.OnChatOutEvent;
import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.models.incoming.chat.OHChatIn;
import com.simonflarup.gearth.origins.models.outgoing.chat.OHChatOut;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.Getter;
import lombok.ToString;

class ChatIntercept extends AbstractIntercept {

    static void onChatIn(HMessage hMessage, OHContext context) {
        ShockPacketIncoming packetIncoming = ShockPacketUtils.getShockPacketIncomingFromMessage(hMessage);
        if (packetIncoming == null) {
            return;
        }
        OHChatIn chatIn = new OHChatIn(packetIncoming, context.getPacketInfoManager());
        context.getEventSystem().post(new OnChatInEventImpl(chatIn, hMessage));
    }

    static void onChatOut(HMessage hMessage, OHContext context) {
        ShockPacketOutgoing packet = ShockPacketUtils.getShockPacketOutgoingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        OHChatOut chatOut = new OHChatOut(packet, context.getPacketInfoManager());
        context.getEventSystem().post(new OnChatOutEventImpl(chatOut, hMessage));
    }

    @ToString(exclude = "hMessage")
    private static class OnChatOutEventImpl implements OnChatOutEvent {
        @Getter
        private final OHChatOut chatOut;
        private final HMessage hMessage;

        public OnChatOutEventImpl(OHChatOut chatOut, HMessage hMessage) {
            this.chatOut = chatOut;
            this.hMessage = hMessage;
        }

        @Override
        public void silenceMessage() {
            hMessage.setBlocked(true);
        }
    }

    @ToString(exclude = "hMessage")
    private static class OnChatInEventImpl implements OnChatInEvent {
        @Getter
        private final OHChatIn chatIn;
        private final HMessage hMessage;

        public OnChatInEventImpl(OHChatIn chatIn, HMessage hMessage) {
            this.chatIn = chatIn;
            this.hMessage = hMessage;
        }

        @Override
        public void silenceMessage() {
            hMessage.setBlocked(true);
        }
    }
}

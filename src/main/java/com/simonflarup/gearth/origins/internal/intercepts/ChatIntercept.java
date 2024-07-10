package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.chat.OnChatEvent;
import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.models.outgoing.chat.OHChatOut;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.Getter;
import lombok.ToString;

class ChatIntercept extends AbstractIntercept {

    static void onChat(HMessage hMessage, OHContext context) {
        ShockPacketOutgoing packet = ShockPacketUtils.getShockPacketOutgoingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        OHChatOut chatOut = new OHChatOut(packet, context.getPacketInfoManager());
        context.getEventSystem().post(new OnChatEventImpl(chatOut, hMessage));
    }

    @ToString(exclude = "hMessage")
    private static class OnChatEventImpl implements OnChatEvent {
        @Getter
        private final OHChatOut chatOut;
        private final HMessage hMessage;

        public OnChatEventImpl(OHChatOut chatOut, HMessage hMessage) {
            this.chatOut = chatOut;
            this.hMessage = hMessage;
        }

        @Override
        public void silenceMessage() {
            hMessage.setBlocked(true);
        }
    }
}

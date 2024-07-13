package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.chat.OnChatInEvent;
import com.simonflarup.gearth.origins.events.chat.OnChatOutEvent;
import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.internal.packets.OHMessageOut;
import com.simonflarup.gearth.origins.models.incoming.chat.OHChatIn;
import com.simonflarup.gearth.origins.models.outgoing.chat.OHChatOut;

class ChatIntercept extends AbstractIntercept {

    static void onChatIn(OHMessageIn message) {
        OHContext context = message.getContext();
        OHChatIn chatIn = new OHChatIn(message.getPacket(), context.getPacketInfoManager());
        context.getEventSystem().post(new OnChatInEventImpl(chatIn, message));
    }

    static void onChatOut(OHMessageOut message) {
        OHContext context = message.getContext();
        OHChatOut chatOut = new OHChatOut(message.getPacket(), context.getPacketInfoManager());
        context.getEventSystem().post(new OnChatOutEventImpl(chatOut, message));
    }

    private static class OnChatInEventImpl extends OHEventImpl<OHChatIn, OHMessageIn> implements OnChatInEvent {

        public OnChatInEventImpl(OHChatIn chatIn, OHMessageIn message) {
            super(chatIn, message);
        }
    }

    private static class OnChatOutEventImpl extends OHEventImpl<OHChatOut, OHMessageOut> implements OnChatOutEvent {
        public OnChatOutEventImpl(OHChatOut chatOut, OHMessageOut message) {
            super(chatOut, message);
        }
    }
}

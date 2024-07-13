package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.trade.*;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.internal.packets.OHMessageOut;
import com.simonflarup.gearth.origins.models.incoming.room.OHTradeAccepted;
import com.simonflarup.gearth.origins.models.incoming.room.OHTradeClosed;
import com.simonflarup.gearth.origins.models.incoming.room.OHTradeCompleted;
import com.simonflarup.gearth.origins.models.incoming.room.OHTradeItems;
import com.simonflarup.gearth.origins.models.outgoing.room.OHAcceptTrade;
import com.simonflarup.gearth.origins.models.outgoing.room.OHAddTradeItem;
import com.simonflarup.gearth.origins.models.outgoing.room.OHCloseTrade;
import com.simonflarup.gearth.origins.models.outgoing.room.OHOpenTrade;

public class TradeIntercept extends AbstractIntercept {
    static void onTradeOpen(OHMessageOut message) {
        OHOpenTrade tradeOpen = new OHOpenTrade(message.getPacket());
        message.getContext().getEventSystem().post(new OnOpenTradeEventImpl(tradeOpen, message));
    }

    static void onTradeAddItem(OHMessageOut message) {
        OHAddTradeItem tradeAddItem = new OHAddTradeItem(message.getPacket());
        message.getContext().getEventSystem().post(new OnAddTradeItemEventImpl(tradeAddItem, message));
    }

    static void onTradeAccept(OHMessageOut message) {
        OHAcceptTrade tradeAccept = new OHAcceptTrade(message.getPacket(), message.getContext().getPacketInfoManager());
        message.getContext().getEventSystem().post(new OnAcceptTradeEventImpl(tradeAccept, message));
    }

    static void onTradeClose(OHMessageOut message) {
        OHCloseTrade tradeClose = new OHCloseTrade();
        message.getContext().getEventSystem().post(new OnCloseTradeEventImpl(tradeClose, message));
    }

    static void onTradeAccepted(OHMessageIn message) {
        OHTradeAccepted tradeAccepted = new OHTradeAccepted(message.getPacket());
        message.getContext().getEventSystem().post(new OnTradeAcceptedEventImpl(tradeAccepted, message));
    }

    static void onTradeClosed(OHMessageIn message) {
        OHTradeClosed tradeClosed = new OHTradeClosed();
        message.getContext().getEventSystem().post(new OnTradeClosedEventImpl(tradeClosed, message));
    }

    static void onTradeCompleted(OHMessageIn message) {
        OHTradeCompleted tradeCompleted = new OHTradeCompleted();
        message.getContext().getEventSystem().post(new OnTradeCompletedEventImpl(tradeCompleted, message));
    }

    static void onTradeItems(OHMessageIn message) {
        OHTradeItems tradeItems = new OHTradeItems(message.getPacket());
        message.getContext().getEventSystem().post(new OnTradeItemsEventImpl(tradeItems, message));
    }

    private static class OnOpenTradeEventImpl extends OHEventImpl<OHOpenTrade, OHMessageOut> implements OnOpenTradeEvent {
        public OnOpenTradeEventImpl(OHOpenTrade data, OHMessageOut message) {
            super(data, message);
        }
    }

    private static class OnAddTradeItemEventImpl extends OHEventImpl<OHAddTradeItem, OHMessageOut> implements OnAddTradeItemEvent {
        public OnAddTradeItemEventImpl(OHAddTradeItem data, OHMessageOut message) {
            super(data, message);
        }
    }

    private static class OnAcceptTradeEventImpl extends OHEventImpl<OHAcceptTrade, OHMessageOut> implements OnAcceptTradeEvent {
        public OnAcceptTradeEventImpl(OHAcceptTrade data, OHMessageOut message) {
            super(data, message);
        }
    }

    private static class OnCloseTradeEventImpl extends OHEventImpl<OHCloseTrade, OHMessageOut> implements OnCloseTradeEvent {
        public OnCloseTradeEventImpl(OHCloseTrade data, OHMessageOut message) {
            super(data, message);
        }
    }

    private static class OnTradeAcceptedEventImpl extends OHEventImpl<OHTradeAccepted, OHMessageIn> implements OnTradeAcceptedEvent {
        public OnTradeAcceptedEventImpl(OHTradeAccepted data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnTradeClosedEventImpl extends OHEventImpl<OHTradeClosed, OHMessageIn> implements OnTradeClosedEvent {
        public OnTradeClosedEventImpl(OHTradeClosed data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnTradeCompletedEventImpl extends OHEventImpl<OHTradeCompleted, OHMessageIn> implements OnTradeCompletedEvent {
        public OnTradeCompletedEventImpl(OHTradeCompleted data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnTradeItemsEventImpl extends OHEventImpl<OHTradeItems, OHMessageIn> implements OnTradeItemsEvent {
        public OnTradeItemsEventImpl(OHTradeItems data, OHMessageIn message) {
            super(data, message);
        }
    }
}

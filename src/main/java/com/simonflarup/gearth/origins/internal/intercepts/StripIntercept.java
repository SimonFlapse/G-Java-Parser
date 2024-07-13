package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.strip.OnStripItemAddedEvent;
import com.simonflarup.gearth.origins.internal.packets.OHMessageOut;
import com.simonflarup.gearth.origins.models.outgoing.room.OHAddStripItem;

public class StripIntercept extends AbstractIntercept {
    static void onAddStripItem(OHMessageOut message) {
        OHAddStripItem stripItem = new OHAddStripItem(message.getPacket());
        message.getContext().getEventSystem().post(new OnStripItemAddedEventImpl(stripItem, message));
    }

    private static class OnStripItemAddedEventImpl extends OHEventImpl<OHAddStripItem, OHMessageOut> implements OnStripItemAddedEvent {
        public OnStripItemAddedEventImpl(OHAddStripItem data, OHMessageOut message) {
            super(data, message);
        }
    }
}

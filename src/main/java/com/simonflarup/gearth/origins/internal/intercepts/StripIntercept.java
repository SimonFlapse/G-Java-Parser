package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.strip.OnGetStripEvent;
import com.simonflarup.gearth.origins.events.strip.OnStripInfoEvent;
import com.simonflarup.gearth.origins.events.strip.OnStripItemAddedEvent;
import com.simonflarup.gearth.origins.events.strip.OnStripItemRemovedEvent;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.internal.packets.OHMessageOut;
import com.simonflarup.gearth.origins.models.incoming.room.OHStripInfo;
import com.simonflarup.gearth.origins.models.outgoing.room.OHAddStripItem;
import com.simonflarup.gearth.origins.models.outgoing.room.OHGetStrip;

public class StripIntercept extends AbstractIntercept {
    static void onAddStripItem(OHMessageOut message) {
        OHAddStripItem stripItem = new OHAddStripItem(message.getPacket());
        message.getContext().getEventSystem().post(new OnStripItemAddedEventImpl(stripItem, message));
    }

    static void onRemoveStripItem(OHMessageIn message) {
        int id = message.getPacket().readInteger();
        message.getContext().getEventSystem().post(new OnStripItemRemovedEventImpl(id, message));
    }

    static void onGetStrip(OHMessageOut message) {
        OHGetStrip getStrip = new OHGetStrip(message.getPacket());
        message.getContext().getEventSystem().post(new OnGetStripEventImpl(getStrip, message));
    }

    static void onStripInfo(OHMessageIn message) {
        OHStripInfo.OHStripInfos stripInfos = OHStripInfo.parse(message.getPacket());
        message.getContext().getEventSystem().post(new OnStripInfoEventImpl(stripInfos, message));
    }

    private static class OnStripItemAddedEventImpl extends OHEventImpl<OHAddStripItem, OHMessageOut> implements OnStripItemAddedEvent {
        public OnStripItemAddedEventImpl(OHAddStripItem data, OHMessageOut message) {
            super(data, message);
        }
    }

    private static class OnStripItemRemovedEventImpl extends OHEventImpl<Integer, OHMessageIn> implements OnStripItemRemovedEvent {
        public OnStripItemRemovedEventImpl(Integer data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnGetStripEventImpl extends OHEventImpl<OHGetStrip, OHMessageOut> implements OnGetStripEvent {
        public OnGetStripEventImpl(OHGetStrip data, OHMessageOut message) {
            super(data, message);
        }
    }

    private static class OnStripInfoEventImpl extends OHEventImpl<OHStripInfo.OHStripInfos, OHMessageIn> implements OnStripInfoEvent {
        public OnStripInfoEventImpl(OHStripInfo.OHStripInfos data, OHMessageIn message) {
            super(data, message);
        }
    }
}

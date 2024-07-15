package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.item.*;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.internal.packets.OHMessageOut;
import com.simonflarup.gearth.origins.models.incoming.room.OHItem;
import com.simonflarup.gearth.origins.models.outgoing.room.OHLoadItems;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

class ItemsIntercept extends AbstractIntercept {
    static void onItemsAdd(OHMessageIn message) {
        OHItem item = new OHItem(message.getPacket());
        message.getContext().getEventSystem().post(new OnItemAddedEventImpl(item, message));
    }

    static void onItemsUpdate(OHMessageIn message) {
        OHItem item = new OHItem(message.getPacket());
        message.getContext().getEventSystem().post(new OnItemUpdatedEventImpl(item, message));
    }

    static void onItemsRemove(OHMessageIn message) {
        ShockPacketIncoming packet = message.getPacket();

        String rawMessage = ShockPacketUtils.getRawMessage(packet);
        int id = Integer.parseInt(rawMessage);

        message.getContext().getEventSystem().post(new OnItemRemovedEventImpl(id, message));
    }

    static void onItems(OHMessageIn message) {
        OHItem[] items = OHItem.parse(message.getPacket());
        message.getContext().getEventSystem().post(new OnItemsLoadedEventImpl(items, message));
    }

    static void onGetItems(OHMessageOut message) {
        OHLoadItems loadItems = new OHLoadItems(message.getPacket(), message.getContext().getPacketInfoManager());
        message.getContext().getEventSystem().post(new OnLoadItemsEventImpl(loadItems, message));
    }

    private static class OnItemAddedEventImpl extends OHEventImpl<OHItem, OHMessageIn> implements OnItemAddedEvent {
        public OnItemAddedEventImpl(OHItem data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnItemUpdatedEventImpl extends OHEventImpl<OHItem, OHMessageIn> implements OnItemUpdatedEvent {
        public OnItemUpdatedEventImpl(OHItem data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnItemRemovedEventImpl extends OHEventImpl<Integer, OHMessageIn> implements OnItemRemovedEvent {
        public OnItemRemovedEventImpl(Integer data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnItemsLoadedEventImpl extends OHEventImpl<OHItem[], OHMessageIn> implements OnItemsLoadedEvent {
        public OnItemsLoadedEventImpl(OHItem[] data, OHMessageIn message) {
            super(data, message);
        }
    }

}

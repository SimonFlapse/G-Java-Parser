package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.item.OnItemAddedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemRemovedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemUpdatedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemsLoadedEvent;
import com.simonflarup.gearth.origins.internal.events.EventPublisher;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.models.incoming.room.OHItem;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

import java.nio.charset.StandardCharsets;

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

        int headerBytes = 2;
        final byte[] dataRemainder = packet.readBytes(packet.getBytesLength() - headerBytes, headerBytes);
        String data = new String(dataRemainder, StandardCharsets.ISO_8859_1);
        int id = Integer.parseInt(data);

        message.getContext().getEventSystem().post(new OnItemRemovedEventImpl(id, message));
    }

    static void onItems(OHMessageIn message) {
        ShockPacketIncoming packet = message.getPacket();
        EventPublisher eventSystem = message.getContext().getEventSystem();

        if (packet.length() > 2) {
            OHItem[] items = OHItem.parse(packet);
            eventSystem.post(new OnItemsLoadedEventImpl(items, message));
        }
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

package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.item.OnItemAddedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemRemovedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemUpdatedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemsLoadedEvent;
import com.simonflarup.gearth.origins.internal.events.EventPublisher;
import com.simonflarup.gearth.origins.models.incoming.room.OHItem;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

import java.nio.charset.StandardCharsets;

class ItemsIntercept extends AbstractIntercept {
    static void onItemsAdd(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        OHItem item = new OHItem(packet);
        eventPublisher.post((OnItemAddedEvent) () -> item);
    }

    static void onItemsUpdate(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        OHItem item = new OHItem(packet);
        eventPublisher.post((OnItemUpdatedEvent) () -> item);
    }

    static void onItemsRemove(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        int headerBytes = 2;
        final byte[] dataRemainder = packet.readBytes(packet.getBytesLength() - headerBytes, headerBytes);
        String data = new String(dataRemainder, StandardCharsets.ISO_8859_1);
        int id = Integer.parseInt(data);
        eventPublisher.post((OnItemRemovedEvent) () -> id);
    }

    static void onItems(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        if (packet.length() > 2) {
            OHItem[] items = OHItem.parse(packet);
            eventPublisher.post((OnItemsLoadedEvent) () -> items);
        }
    }
}

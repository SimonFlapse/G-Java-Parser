package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.strip.OnStripItemAddedEvent;
import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.models.outgoing.room.OHAddStripItem;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;

public class StripIntercept extends AbstractIntercept {
    static void onAddStripItem(ShockPacketOutgoing packet, OHContext context) {
        OHAddStripItem stripItem = new OHAddStripItem(packet);
        context.getEventSystem().post((OnStripItemAddedEvent) () -> stripItem);
    }
}

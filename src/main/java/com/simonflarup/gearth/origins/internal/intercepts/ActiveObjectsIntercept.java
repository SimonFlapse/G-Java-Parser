package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.activeobject.*;
import com.simonflarup.gearth.origins.internal.events.EventPublisher;
import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

class ActiveObjectsIntercept extends AbstractIntercept {
    static void onActiveObjectsAdd(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        OHActiveObject activeObject = new OHActiveObject(packet);
        eventPublisher.post((OnActiveObjectAddedEvent) () -> activeObject);
    }


    static void onActiveObjectsUpdate(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        OHActiveObject activeObject = new OHActiveObject(packet);
        eventPublisher.post((OnActiveObjectUpdatedEvent) () -> activeObject);
    }

    static void onActiveObjectsRemove(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        OHActiveObject activeObject = new OHActiveObject(packet);
        eventPublisher.post((OnActiveObjectRemovedEvent) () -> activeObject);
    }

    static void onActiveObjects(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        OHActiveObject[] activeObjects = OHActiveObject.parse(packet);
        eventPublisher.post((OnActiveObjectsLoadedEvent) () -> activeObjects);
    }

    static void onStuffDataUpdate(ShockPacketIncoming packet, EventPublisher eventPublisher) {
        int furniId = Integer.parseInt(packet.readString());
        String stuffData = packet.readString();
        eventPublisher.post(new OnStuffDataUpdatedEvent() {
            @Override
            public int getTargetId() {
                return furniId;
            }

            @Override
            public String getNewData() {
                return stuffData;
            }
        });
    }
}

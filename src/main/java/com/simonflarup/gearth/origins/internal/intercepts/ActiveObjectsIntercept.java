package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.activeobject.*;
import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

class ActiveObjectsIntercept extends AbstractIntercept {
    static void onActiveObjectsAdd(ShockPacketIncoming packet, OHContext context) {
        OHActiveObject activeObject = new OHActiveObject(packet);
        context.getEventSystem().post((OnActiveObjectAddedEvent) () -> activeObject);
    }


    static void onActiveObjectsUpdate(ShockPacketIncoming packet, OHContext context) {
        OHActiveObject activeObject = new OHActiveObject(packet);
        context.getEventSystem().post((OnActiveObjectUpdatedEvent) () -> activeObject);
    }

    static void onActiveObjectsRemove(ShockPacketIncoming packet, OHContext context) {
        OHActiveObject activeObject = new OHActiveObject(packet);
        context.getEventSystem().post((OnActiveObjectRemovedEvent) () -> activeObject);
    }

    static void onActiveObjects(ShockPacketIncoming packet, OHContext context) {
        OHActiveObject[] activeObjects = OHActiveObject.parse(packet);
        context.getEventSystem().post((OnActiveObjectsLoadedEvent) () -> activeObjects);
    }

    static void onStuffDataUpdate(ShockPacketIncoming packet, OHContext context) {
        int furniId = Integer.parseInt(packet.readString());
        String stuffData = packet.readString();
        context.getEventSystem().post(new OnStuffDataUpdatedEvent() {
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

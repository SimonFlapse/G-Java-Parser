package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.activeobject.*;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;
import com.simonflarup.gearth.origins.models.incoming.room.OHStuffData;

class ActiveObjectsIntercept extends AbstractIntercept {
    static void onActiveObjectsAdd(OHMessageIn message) {
        OHActiveObject activeObject = new OHActiveObject(message.getPacket());
        message.getContext().getEventSystem().post(new OnActiveObjectAddedEventImpl(activeObject, message) {
        });
    }

    static void onActiveObjectsUpdate(OHMessageIn message) {
        OHActiveObject activeObject = new OHActiveObject(message.getPacket());
        message.getContext().getEventSystem().post(new OnActiveObjectUpdatedEventImpl(activeObject, message));
    }

    static void onActiveObjectsRemove(OHMessageIn message) {
        OHActiveObject activeObject = new OHActiveObject(message.getPacket());
        message.getContext().getEventSystem().post(new OnActiveObjectRemovedEventImpl(activeObject, message));
    }

    static void onActiveObjects(OHMessageIn message) {
        OHActiveObject[] activeObjects = OHActiveObject.parse(message.getPacket());
        message.getContext().getEventSystem().post(new OnActiveObjectsLoadedEventImpl(activeObjects, message));
    }

    static void onStuffDataUpdate(OHMessageIn message) {
        OHStuffData stuffData = new OHStuffData(message);
        message.getContext().getEventSystem().post(new OnStuffDataUpdatedEventImpl(stuffData, message));
    }

    private static class OnActiveObjectAddedEventImpl extends OHEventImpl<OHActiveObject, OHMessageIn> implements OnActiveObjectAddedEvent {
        public OnActiveObjectAddedEventImpl(OHActiveObject data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnActiveObjectUpdatedEventImpl extends OHEventImpl<OHActiveObject, OHMessageIn> implements OnActiveObjectUpdatedEvent {
        public OnActiveObjectUpdatedEventImpl(OHActiveObject data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnActiveObjectRemovedEventImpl extends OHEventImpl<OHActiveObject, OHMessageIn> implements OnActiveObjectRemovedEvent {
        public OnActiveObjectRemovedEventImpl(OHActiveObject data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnActiveObjectsLoadedEventImpl extends OHEventImpl<OHActiveObject[], OHMessageIn> implements OnActiveObjectsLoadedEvent {
        public OnActiveObjectsLoadedEventImpl(OHActiveObject[] data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnStuffDataUpdatedEventImpl extends OHEventImpl<OHStuffData, OHMessageIn> implements OnStuffDataUpdatedEvent {
        public OnStuffDataUpdatedEventImpl(OHStuffData data, OHMessageIn message) {
            super(data, message);
        }
    }
}

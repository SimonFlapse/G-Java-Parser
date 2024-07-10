package com.simonflarup.gearth.origins.events.type.activeobject;

import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;

public interface OnActiveObjectRemovedEvent {
    OHActiveObject getActiveObject();
}

package com.simonflarup.gearth.origins.events.activeobject;

import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;

public interface OnActiveObjectsLoadedEvent {
    OHActiveObject[] getActiveObjects();
}

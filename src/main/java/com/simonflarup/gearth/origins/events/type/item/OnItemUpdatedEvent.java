package com.simonflarup.gearth.origins.events.type.item;

import com.simonflarup.gearth.origins.models.incoming.room.OHItem;

public interface OnItemUpdatedEvent {
    OHItem getItem();
}

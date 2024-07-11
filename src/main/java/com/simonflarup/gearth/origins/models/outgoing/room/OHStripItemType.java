package com.simonflarup.gearth.origins.models.outgoing.room;

import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;
import com.simonflarup.gearth.origins.models.incoming.room.OHItem;

public enum OHStripItemType {
    ITEM,
    STUFF;

    public static OHStripItemType getFrom(OHItem item) {
        return ITEM;
    }

    public static OHStripItemType getFrom(OHActiveObject activeObject) {
        return STUFF;
    }
}

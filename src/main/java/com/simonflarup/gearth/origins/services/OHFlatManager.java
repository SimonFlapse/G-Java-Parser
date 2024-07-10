package com.simonflarup.gearth.origins.services;

import com.simonflarup.gearth.origins.models.incoming.navigator.OHFlatInfo;
import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;
import com.simonflarup.gearth.origins.models.incoming.room.OHItem;

import java.util.Map;

public interface OHFlatManager {
    OHFlatInfo getCurrentFlatInfo();
    Map<Integer, OHActiveObject> getActiveObjectsInFlat();
    Map<Integer, OHItem> getItemsInFlat();
}

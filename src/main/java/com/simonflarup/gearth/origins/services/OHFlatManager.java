package com.simonflarup.gearth.origins.services;

import com.simonflarup.gearth.origins.models.incoming.navigator.OHFlatInfo;
import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;
import com.simonflarup.gearth.origins.models.incoming.room.OHItem;

import java.util.Map;

/**
 * <h3>Flat Manager</h3>
 * <p>Stores information about the current room including dynamic map of items in the room</p>
 * <p>Available from the {@link OHServiceProvider}</p>
 *
 * @see OHServiceProvider
 */
public interface OHFlatManager {
    /**
     * <h3>Current flat information</h3>
     * @return {@link OHFlatInfo} object containing information about the current room
     */
    OHFlatInfo getCurrentFlatInfo();

    /**
     * <h3>Floor items in room</h3>
     * <p>Get a map of all floor items in the current room</p>
     * <p>The underlying data is dynamically updated when items are added, moved or removed from the room, so avoid storing the map</p>
     * @return {@link Map} of floor items in the current room
     * @see #getItemsInFlat()
     */
    Map<Integer, OHActiveObject> getActiveObjectsInFlat();

    /**
     * <h3>Wall items in room</h3>
     * <p>Get a map of all wall items in the current room</p>
     * <p>The underlying data is dynamically updated when items are added, moved or removed from the room, so avoid storing the map</p>
     * @return {@link Map} of wall items in the current room
     * @see #getActiveObjectsInFlat()
     */
    Map<Integer, OHItem> getItemsInFlat();
}

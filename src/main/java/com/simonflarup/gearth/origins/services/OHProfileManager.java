package com.simonflarup.gearth.origins.services;

import com.simonflarup.gearth.origins.models.incoming.entry.OHUserObject;

import java.util.Optional;

/**
 * <h3>Profile Manager</h3>
 * <p>Stores information about the current client user</p>
 * <p>Available from the {@link OHServiceProvider}</p>
 *
 * @see OHServiceProvider
 */
public interface OHProfileManager {
    /**
     * <h3>Current User</h3>
     *
     * @return {@link Optional<OHUserObject>} optional containing information about the profile of the current user or empty if the information is not available
     */
    Optional<OHUserObject> getCurrentUser();
}

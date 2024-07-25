package com.simonflarup.gearth.origins.services;

/**
 * <h3>Service Provider</h3>
 * <p>The service provider holds common services managed by G-Java-Parser that can be used to interact with Habbo Hotel: Origins</p>
 */
public interface OHServiceProvider {
    /**
     * <h3>Flat manager</h3>
     * <p>Keeps track of room information and items</p>
     *
     * @return The flat manager managed by G-Java-Parser
     */
    OHFlatManager getFlatManager();

    /**
     * <h3>Profile manager</h3>
     * <p>Keeps track of information about the active user profile</p>
     *
     * @return the profile manager managed by G-Java-Parser
     */
    OHProfileManager getProfileManager();

    /**
     * <h3>Packet sender</h3>
     * <p>Allows for sending packets to the server or client using the G-Java-Parser models</p>
     * @return The packet sender provided by G-Java-Parser
     */
    OHPacketSender getPacketSender();
}

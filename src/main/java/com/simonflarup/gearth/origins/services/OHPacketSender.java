package com.simonflarup.gearth.origins.services;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;

/**
 * <h3>Packet Sender</h3>
 * <p>Interface for sending packets to the server and client.</p>
 */
public interface OHPacketSender {
    /**
     * <h3>Send to Server</h3>
     * <p>Send a packet to the server.</p>
     * <p>
     * Most parser models implement {@link OHServerPacket} or provide methods to fetch a specific {@link OHServerPacket}
     * including a constructor that takes the required information to construct a valid packet.
     * <br/><br/>
     * <i>It is not intended to create the packets manually.</i>
     * </p>
     * <h4>Example:</h4>
     * <pre>
     *      {@code
     *          OHAddStripItem addStripItem = new OHAddStripItem(someId, itemType);
     *          packetSender.toServer(addStripItem);
     *      }
     *  </pre>
     *
     * @param packet The packet to send.
     * @return True if the packet was sent, false if not.
     * @see #toClient(OHClientPacket)
     */
    boolean toServer(OHServerPacket packet);

    /**
     * <h3>Send to Client</h3>
     * <p>Send a packet to the client.</p>
     * <p>
     *     Most parser models implement {@link OHClientPacket} or provide methods to fetch a specific {@link OHClientPacket}
     *     including a constructor that takes the required information to construct a valid packet.
     *     <br/><br/>
     *     <i>It is not intended to create the packets manually.</i>
     * </p>
     * <h4>Example:</h4>
     * <p>This example uses a parser model that have multiple {@link OHClientPacket} implementations available</p>
     * <p>In that case we have to invoke methods on the model to get the correct {@link OHClientPacket}</p>
     * <p>In this example we are making a floor item appear to have disappeared for the client whenever it has been rotated, moved etc.</p>
     * <pre>
     *     {@code
     *          @Subscribe
     *          public void onActiveObjectUpdated(OnActiveObjectUpdatedEvent event) {
     *              OHActiveObject activeObject = event.getActiveObject();
     *              // Make it seem the object was removed from the room (Only applies client side)
     *              packetSender.toClient(activeObject.getActiveObjectRemovePacket());
     *          }
     *     }
     * </pre>
     *
     * @param packet The packet to send.
     * @return True if the packet was sent, false if not.
     * @see #toServer(OHServerPacket)
     */
    boolean toClient(OHClientPacket packet);
}

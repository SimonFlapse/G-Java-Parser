package com.simonflarup.gearth.origins.services;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;

import java.util.function.Consumer;

/**
 * <h3>Packet Sender</h3>
 * <p>Interface for sending packets to the server and client.</p>
 */
public interface OHPacketSender {
    /**
     * @deprecated This method is deprecated and will be removed in the future. Use {@link #scheduleToServer(OHServerPacket)} instead.
     */
    @Deprecated
    boolean toServer(OHServerPacket packet);

    /**
     * @deprecated This method is deprecated and will be removed in the future. Use {@link #scheduleToClient(OHClientPacket)} instead.
     */
    @Deprecated
    boolean toClient(OHClientPacket packet);

    /**
     * <h3>Schedule send to Server</h3>
     * <p>Schedule a packet to the server that is send at regular intervals to prevent rate limiting.</p>
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
     *          packetSender.scheduleToServer(addStripItem);
     *      }
     *  </pre>
     *
     * @param packet The packet to send.
     * @see #scheduleToServer(OHServerPacket, Consumer)
     * @see #scheduleToClient(OHClientPacket)
     */
    void scheduleToServer(OHServerPacket packet);

    /**
     * <h3>Schedule send to Server</h3>
     * <p>Schedule a packet to the server with a consumer that is invoked when the packet has been sent</p>
     * <h4>Example:</h4>
     *        <pre>
     *             {@code
     *                 private void sendPacket() {
     *                     OHAddStripItem addStripItem = new OHAddStripItem(someId, itemType);
     *                     packetSender.scheduleToServer(addStripItem, (success) -> System.out.println("Sent: " + success));
     *                 }
     *             }
     *         </pre>
     * @param packet The packet to send.
     * @param completionConsumer A consumer that is invoked with the completion status of the packet. True if the packet was sent successfully, false otherwise.
     */
    void scheduleToServer(OHServerPacket packet, Consumer<Boolean> completionConsumer);

    /**
     * <h3>Schedule send to Client</h3>
     * <p>Schedule a packet to the client that is send at regular intervals</p>
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
     *              packetSender.scheduleToClient(activeObject.getActiveObjectRemovePacket());
     *          }
     *     }
     * </pre>
     *
     * @param packet The packet to send.
     * @see #scheduleToClient(OHClientPacket, Consumer)
     * @see #scheduleToServer(OHServerPacket)
     */
    void scheduleToClient(OHClientPacket packet);

    /**
     * <h3>Schedule send to Client</h3>
     * <p>Schedule a packet to the client with a consumer that is invoked when the packet has been sent</p>
     * <h4>Example:</h4>
     * <pre>
     *             {@code
     *                 private void sendPacket() {
     *                     OHAddStripItem addStripItem = new OHAddStripItem(someId, itemType);
     *                     packetSender.scheduleToServer(addStripItem, (success) -> System.out.println("Sent: " + success));
     *                 }
     *             }
     *         </pre>
     *
     * @param packet             The packet to send.
     * @param completionConsumer A consumer that is invoked with the completion status of the packet. True if the packet was sent successfully, false otherwise.
     */
    void scheduleToClient(OHClientPacket packet, Consumer<Boolean> completionConsumer);
}

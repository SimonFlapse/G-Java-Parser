package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.InternalExtensionProvider;
import com.simonflarup.gearth.origins.internal.events.EventPublisher;
import com.simonflarup.gearth.origins.internal.events.IncomingPacketHandler;
import com.simonflarup.gearth.origins.internal.events.OutgoingPacketHandler;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.extensions.Extension;
import gearth.extensions.ExtensionBase;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OHInterceptor {
    private final Extension extension;
    private final EventPublisher eventPublisher;

    public OHInterceptor(EventPublisher eventPublisher, InternalExtensionProvider extension) {
        this.extension = extension.getExtension();
        this.eventPublisher = eventPublisher;
    }

    private static ExtensionBase.MessageListener safeInvoke(ExtensionBase.MessageListener listener) {
        return (hMessage -> {
            try {
                listener.act(hMessage);
            } catch (Exception e) {
                log.error("Unhandled exception in interceptor", e);
            }
        });
    }

    public void setupInterceptors() {
        interceptToClient("ITEMS", (message) -> ItemsIntercept.onItems(message, eventPublisher));
        interceptToClient("ITEMS_2", (message) -> ItemsIntercept.onItemsAdd(message, eventPublisher));
        interceptToClient("UPDATEITEM", (message) -> ItemsIntercept.onItemsUpdate(message, eventPublisher));
        interceptToClient("REMOVEITEM", (message) -> ItemsIntercept.onItemsRemove(message, eventPublisher));
        interceptToClient("ACTIVEOBJECTS", (message) -> ActiveObjectsIntercept.onActiveObjects(message, eventPublisher));
        interceptToClient("ACTIVEOBJECT_ADD", (message) -> ActiveObjectsIntercept.onActiveObjectsAdd(message, eventPublisher));
        interceptToClient("ACTIVEOBJECT_UPDATE", (message) -> ActiveObjectsIntercept.onActiveObjectsUpdate(message, eventPublisher));
        interceptToClient("ACTIVEOBJECT_REMOVE", (message) -> ActiveObjectsIntercept.onActiveObjectsRemove(message, eventPublisher));
        interceptToClient("STUFFDATAUPDATE", (message) -> ActiveObjectsIntercept.onStuffDataUpdate(message, eventPublisher));
        interceptToClient("FLATINFO", (message) -> FlatIntercept.onFlatInfo(message, eventPublisher));

        // The chat intercept needs the original hMessage to block it from being sent to the server
        extension.intercept(HMessage.Direction.TOSERVER, "CHAT", safeInvoke((message) -> ChatIntercept.onChat(message, eventPublisher)));
    }

    private void interceptToClient(String header, IncomingPacketHandler incomingPacketHandler) {
        extension.intercept(HMessage.Direction.TOCLIENT, header, safeInvoke((hMessage) -> handleEvent(hMessage, incomingPacketHandler)));
    }

    private void handleEvent(HMessage hMessage, IncomingPacketHandler incomingPacketHandler) {
        ShockPacketIncoming packet = ShockPacketUtils.getShockPacketIncomingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        incomingPacketHandler.handlePacket(packet);
    }

    private void interceptToServer(String header, OutgoingPacketHandler outgoingPacketHandler) {
        extension.intercept(HMessage.Direction.TOSERVER, header, safeInvoke((hMessage) -> handleEvent(hMessage, outgoingPacketHandler)));
    }

    private void handleEvent(HMessage hMessage, OutgoingPacketHandler outgoingPacketHandler) {
        ShockPacketOutgoing packet = ShockPacketUtils.getShockPacketOutgoingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        outgoingPacketHandler.handlePacket(packet);
    }
}

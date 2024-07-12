package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.OHExtension;
import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.internal.events.IncomingPacketHandler;
import com.simonflarup.gearth.origins.internal.events.OutgoingPacketHandler;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.extensions.ExtensionBase;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OHInterceptor {
    private final OHContext context;

    public OHInterceptor(OHContext context) {
        this.context = context;
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
        interceptToClient("ITEMS", (message) -> ItemsIntercept.onItems(message, context));
        interceptToClient("ITEMS_2", (message) -> ItemsIntercept.onItemsAdd(message, context));
        interceptToClient("UPDATEITEM", (message) -> ItemsIntercept.onItemsUpdate(message, context));
        interceptToClient("REMOVEITEM", (message) -> ItemsIntercept.onItemsRemove(message, context));
        interceptToClient("ACTIVEOBJECTS", (message) -> ActiveObjectsIntercept.onActiveObjects(message, context));
        interceptToClient("ACTIVEOBJECT_ADD", (message) -> ActiveObjectsIntercept.onActiveObjectsAdd(message, context));
        interceptToClient("ACTIVEOBJECT_UPDATE", (message) -> ActiveObjectsIntercept.onActiveObjectsUpdate(message, context));
        interceptToClient("ACTIVEOBJECT_REMOVE", (message) -> ActiveObjectsIntercept.onActiveObjectsRemove(message, context));
        interceptToClient("STUFFDATAUPDATE", (message) -> ActiveObjectsIntercept.onStuffDataUpdate(message, context));
        interceptToClient("FLATINFO", (message) -> FlatIntercept.onFlatInfo(message, context));

        interceptToServer("ADDSTRIPITEM", (message) -> StripIntercept.onAddStripItem(message, context));

        OHExtension extension = context.getExtension();
        // The chat intercept needs the original hMessage to block it from being sent to the server
        extension.intercept(HMessage.Direction.TOSERVER, "CHAT", safeInvoke((message) -> ChatIntercept.onChatOut(message, context)));
        extension.intercept(HMessage.Direction.TOSERVER, "WHISPER", safeInvoke((message) -> ChatIntercept.onChatOut(message, context)));
        extension.intercept(HMessage.Direction.TOSERVER, "SHOUT", safeInvoke((message) -> ChatIntercept.onChatOut(message, context)));

        extension.intercept(HMessage.Direction.TOCLIENT, "CHAT", safeInvoke((message) -> ChatIntercept.onChatIn(message, context)));
        extension.intercept(HMessage.Direction.TOCLIENT, "CHAT_2", safeInvoke((message) -> ChatIntercept.onChatIn(message, context)));
        extension.intercept(HMessage.Direction.TOCLIENT, "CHAT_3", safeInvoke((message) -> ChatIntercept.onChatIn(message, context)));
    }

    private void interceptToClient(String header, IncomingPacketHandler incomingPacketHandler) {
        context.getExtension().intercept(HMessage.Direction.TOCLIENT, header, safeInvoke((hMessage) -> handleEvent(hMessage, incomingPacketHandler)));
    }

    private void handleEvent(HMessage hMessage, IncomingPacketHandler incomingPacketHandler) {
        ShockPacketIncoming packet = ShockPacketUtils.getShockPacketIncomingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        incomingPacketHandler.handlePacket(packet);
    }

    private void interceptToServer(String header, OutgoingPacketHandler outgoingPacketHandler) {
        context.getExtension().intercept(HMessage.Direction.TOSERVER, header, safeInvoke((hMessage) -> handleEvent(hMessage, outgoingPacketHandler)));
    }

    private void handleEvent(HMessage hMessage, OutgoingPacketHandler outgoingPacketHandler) {
        ShockPacketOutgoing packet = ShockPacketUtils.getShockPacketOutgoingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        outgoingPacketHandler.handlePacket(packet);
    }
}

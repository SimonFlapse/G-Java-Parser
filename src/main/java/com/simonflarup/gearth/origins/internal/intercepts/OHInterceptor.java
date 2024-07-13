package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.internal.packets.*;
import gearth.extensions.ExtensionBase;
import gearth.protocol.HMessage;
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
        interceptToClient("ITEMS", ItemsIntercept::onItems);
        interceptToClient("ITEMS_2", ItemsIntercept::onItemsAdd);
        interceptToClient("UPDATEITEM", ItemsIntercept::onItemsUpdate);
        interceptToClient("REMOVEITEM", ItemsIntercept::onItemsRemove);
        interceptToClient("ACTIVEOBJECTS", ActiveObjectsIntercept::onActiveObjects);
        interceptToClient("ACTIVEOBJECT_ADD", ActiveObjectsIntercept::onActiveObjectsAdd);
        interceptToClient("ACTIVEOBJECT_UPDATE", ActiveObjectsIntercept::onActiveObjectsUpdate);
        interceptToClient("ACTIVEOBJECT_REMOVE", ActiveObjectsIntercept::onActiveObjectsRemove);
        interceptToClient("STUFFDATAUPDATE", ActiveObjectsIntercept::onStuffDataUpdate);
        interceptToClient("FLATINFO", FlatIntercept::onFlatInfo);

        interceptToServer("ADDSTRIPITEM", StripIntercept::onAddStripItem);
        interceptToServer("GETSTRIP", StripIntercept::onGetStrip);
        interceptToClient("STRIPINFO_2", StripIntercept::onStripInfo);

        interceptToServer("CHAT", ChatIntercept::onChatOut);
        interceptToServer("WHISPER", ChatIntercept::onChatOut);
        interceptToServer("SHOUT", ChatIntercept::onChatOut);

        interceptToClient("CHAT", ChatIntercept::onChatIn);
        interceptToClient("CHAT_2", ChatIntercept::onChatIn);
        interceptToClient("CHAT_3", ChatIntercept::onChatIn);
    }

    private void interceptToClient(String header, IncomingPacketHandler incomingPacketHandler) {
        context.getExtension().intercept(HMessage.Direction.TOCLIENT, header, safeInvoke((hMessage) -> handleEvent(hMessage, incomingPacketHandler)));
    }

    private void handleEvent(HMessage hMessage, IncomingPacketHandler incomingPacketHandler) {
        try {
            OHMessageIn message = new OHMessageIn(hMessage, context);
            incomingPacketHandler.handlePacket(message);
        } catch (WrongShockPacketFormatException exception) {
            log.error("Failed to handle packet", exception);
        }
    }

    private void interceptToServer(String header, OutgoingPacketHandler outgoingPacketHandler) {
        context.getExtension().intercept(HMessage.Direction.TOSERVER, header, safeInvoke((hMessage) -> handleEvent(hMessage, outgoingPacketHandler)));
    }

    private void handleEvent(HMessage hMessage, OutgoingPacketHandler outgoingPacketHandler) {
        try {
            OHMessageOut message = new OHMessageOut(hMessage, context);
            outgoingPacketHandler.handlePacket(message);
        } catch (WrongShockPacketFormatException exception) {
            log.error("Failed to handle packet", exception);
        }
    }
}

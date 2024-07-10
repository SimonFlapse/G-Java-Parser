package com.simonflarup.gearth.origins;

import com.simonflarup.gearth.origins.events.EventSystem;
import com.simonflarup.gearth.origins.events.IncomingPacketHandler;
import com.simonflarup.gearth.origins.events.OutgoingPacketHandler;
import com.simonflarup.gearth.origins.intercepts.ActiveObjectsIntercept;
import com.simonflarup.gearth.origins.intercepts.ChatIntercept;
import com.simonflarup.gearth.origins.intercepts.FlatIntercept;
import com.simonflarup.gearth.origins.intercepts.ItemsIntercept;
import com.simonflarup.gearth.origins.services.OHServiceProvider;
import com.simonflarup.gearth.origins.services.internal.OHServiceProviderImpl;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.extensions.Extension;
import gearth.extensions.ExtensionBase;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class OHExtension extends Extension {
    private final OHServiceProvider serviceProvider;

    protected OHExtension(String[] args) {
        super(args);
        EventSystem.register(this);
        serviceProvider = new OHServiceProviderImpl();
    }

    public OHServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    @Override
    protected void initExtension() {
        onConnect((host, port, APIVersion, versionClient, client) -> {
            if (!Objects.equals(versionClient, "SHOCKWAVE")) {
                log.error("This extension only works with Habbo Hotel: Origins");
                System.exit(0);
            }
        });

        setupIntercepts();
    }

    private void setupIntercepts() {
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

        // The chat intercept needs the original hMessage to block it from being sent to the server
        intercept(HMessage.Direction.TOSERVER, "CHAT", safeInvoke(ChatIntercept::onChat));
    }

    private void interceptToClient(String header, IncomingPacketHandler incomingPacketHandler) {
        intercept(HMessage.Direction.TOCLIENT, header, safeInvoke((hMessage) -> handleEvent(hMessage, incomingPacketHandler)));
    }

    private void handleEvent(HMessage hMessage, IncomingPacketHandler incomingPacketHandler) {
        ShockPacketIncoming packet = ShockPacketUtils.getShockPacketIncomingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        incomingPacketHandler.handlePacket(packet);
    }

    private void interceptToServer(String header, OutgoingPacketHandler outgoingPacketHandler) {
        intercept(HMessage.Direction.TOSERVER, header, safeInvoke((hMessage) -> handleEvent(hMessage, outgoingPacketHandler)));
    }

    private void handleEvent(HMessage hMessage, OutgoingPacketHandler outgoingPacketHandler) {
        ShockPacketOutgoing packet = ShockPacketUtils.getShockPacketOutgoingFromMessage(hMessage);
        if (packet == null) {
            return;
        }
        outgoingPacketHandler.handlePacket(packet);
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
}

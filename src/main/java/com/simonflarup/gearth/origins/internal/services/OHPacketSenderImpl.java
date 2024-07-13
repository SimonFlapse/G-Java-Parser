package com.simonflarup.gearth.origins.internal.services;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import com.simonflarup.gearth.origins.services.OHPacketSender;
import gearth.extensions.ExtensionBase;

public class OHPacketSenderImpl implements OHPacketSender {

    private final ExtensionBase extension;

    OHPacketSenderImpl(ExtensionBase extension) {
        this.extension = extension;
    }

    @Override
    public boolean toServer(OHServerPacket packet) {
        return extension.sendToServer(packet.getOutgoingPacket(extension.getPacketInfoManager()));
    }

    @Override
    public boolean toClient(OHClientPacket packet) {
        return extension.sendToClient(packet.getIncomingPacket(extension.getPacketInfoManager()));
    }
}

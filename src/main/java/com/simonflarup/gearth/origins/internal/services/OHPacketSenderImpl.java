package com.simonflarup.gearth.origins.internal.services;

import com.simonflarup.gearth.origins.OHExtension;
import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import com.simonflarup.gearth.origins.services.OHPacketSender;

public class OHPacketSenderImpl implements OHPacketSender {

    private final OHExtension extension;

    OHPacketSenderImpl(OHExtension extension) {
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

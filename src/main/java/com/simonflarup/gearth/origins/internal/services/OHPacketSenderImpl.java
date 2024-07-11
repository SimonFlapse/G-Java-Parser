package com.simonflarup.gearth.origins.internal.services;

import com.simonflarup.gearth.origins.OHExtension;
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
}

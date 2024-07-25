package com.simonflarup.gearth.origins.internal.services;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import com.simonflarup.gearth.origins.services.OHPacketSender;
import gearth.extensions.ExtensionBase;

import java.util.function.Consumer;

public class OHPacketSenderImpl implements OHPacketSender {

    private final PacketThrottler packetThrottler;

    OHPacketSenderImpl(ExtensionBase extension) {
        this.packetThrottler = new PacketThrottler(extension);
    }

    @Override
    public boolean toServer(OHServerPacket packet) {
        this.scheduleToServer(packet);
        return true;
    }


    @Override
    public boolean toClient(OHClientPacket packet) {
        this.scheduleToClient(packet);
        return true;
    }

    @Override
    public void scheduleToServer(OHServerPacket packet) {
        this.scheduleToServer(packet, null);
    }

    @Override
    public void scheduleToServer(OHServerPacket packet, Consumer<Boolean> completionConsumer) {
        packetThrottler.addToServerQueue(packet, completionConsumer);
    }

    @Override
    public void scheduleToClient(OHClientPacket packet) {
        this.scheduleToClient(packet, null);
    }

    @Override
    public void scheduleToClient(OHClientPacket packet, Consumer<Boolean> completionConsumer) {
        packetThrottler.addToClientQueue(packet, completionConsumer);
    }
}

package com.simonflarup.gearth.origins.internal.services;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import gearth.extensions.ExtensionBase;
import gearth.protocol.packethandler.shockwave.packets.ShockPacket;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Consumer;

class PacketThrottler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Queue<CompletablePacket<ShockPacketOutgoing>> serverPacketQueue = new ConcurrentLinkedQueue<>();
    private final Queue<CompletablePacket<ShockPacketIncoming>> clientPacketQueue = new ConcurrentLinkedQueue<>();
    private final ExtensionBase extension;

    PacketThrottler(ExtensionBase extension) {
        this.extension = extension;
        schedulePacketSending();
    }

    private void schedulePacketSending() {
        int variablePeriod = ThreadLocalRandom.current().nextInt(400, 750);
        executor.scheduleAtFixedRate(() -> {
            if (!serverPacketQueue.isEmpty()) {
                CompletablePacket<ShockPacketOutgoing> outPacket = serverPacketQueue.poll();
                outPacket.complete(extension.sendToServer(outPacket.getPacket()));
            }
            if (!clientPacketQueue.isEmpty()) {
                CompletablePacket<ShockPacketIncoming> inPacket = clientPacketQueue.poll();
                inPacket.complete(extension.sendToClient(inPacket.getPacket()));
            }
        }, 0, variablePeriod, TimeUnit.MILLISECONDS);
    }

    void addToServerQueue(OHServerPacket packet, Consumer<Boolean> completionConsumer) {
        ShockPacketOutgoing outgoingPacket = packet.getOutgoingPacket(extension.getPacketInfoManager());
        CompletablePacket<ShockPacketOutgoing> completablePacket = new CompletablePacket<>(outgoingPacket, completionConsumer);
        serverPacketQueue.add(completablePacket);
    }

    void addToClientQueue(OHClientPacket packet, Consumer<Boolean> completionConsumer) {
        ShockPacketIncoming incomingPacket = packet.getIncomingPacket(extension.getPacketInfoManager());
        CompletablePacket<ShockPacketIncoming> completablePacket = new CompletablePacket<>(incomingPacket, completionConsumer);
        clientPacketQueue.add(completablePacket);
    }

    @Slf4j
    private static class CompletablePacket<T extends ShockPacket> {
        private final T packet;
        private final Consumer<Boolean> completionConsumer;

        CompletablePacket(T packet, Consumer<Boolean> completionConsumer) {
            this.packet = packet;
            this.completionConsumer = completionConsumer;
        }

        void complete(boolean success) {
            if (!success) {
                log.error("Failed to send packet: {}", packet);
            }

            if (completionConsumer != null) {
                completionConsumer.accept(success);
            }
        }

        T getPacket() {
            return this.packet;
        }
    }
}
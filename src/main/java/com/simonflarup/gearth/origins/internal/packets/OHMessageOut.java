package com.simonflarup.gearth.origins.internal.packets;

import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.utils.ShockPacketUtils;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import lombok.Getter;

public final class OHMessageOut implements OHMessage {
    private final HMessage message;
    @Getter
    private final ShockPacketOutgoing packet;
    @Getter
    private final OHContext context;

    public OHMessageOut(HMessage message, OHContext context) {
        this.message = message;
        this.packet = ShockPacketUtils.getShockPacketOutgoingFromMessage(message);

        if (this.packet == null) {
            throw new WrongShockPacketFormatException("Message is not an incoming packet.");
        }

        this.context = context;
    }

    public boolean isBlocked() {
        return message.isBlocked();
    }

    public void setBlocked(boolean blocked) {
        message.setBlocked(blocked);
    }

    public HMessage.Direction getDestination() {
        return message.getDestination();
    }

    public boolean isCorrupted() {
        return message.isCorrupted();
    }
}

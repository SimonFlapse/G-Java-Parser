package com.simonflarup.gearth.origins.utils;

import gearth.protocol.HMessage;
import gearth.protocol.HPacket;
import gearth.protocol.packethandler.shockwave.packets.ShockPacket;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;

import java.nio.charset.StandardCharsets;

/**
 * <h3>Utility class for ShockPacket-related operations.</h3>
 * <p>Primarily used for class casting.</p>
 */
public final class ShockPacketUtils {
    private ShockPacketUtils() {}

    public static ShockPacketIncoming getShockPacketIncomingFromMessage(HMessage hMessage) {
        HPacket hPacket = hMessage.getPacket();
        if (!(hPacket instanceof ShockPacketIncoming)) {
            return null;
        }
        return (ShockPacketIncoming) hPacket;
    }

    public static ShockPacketOutgoing getShockPacketOutgoingFromMessage(HMessage hMessage) {
        HPacket hPacket = hMessage.getPacket();
        if (!(hPacket instanceof ShockPacketOutgoing)) {
            return null;
        }
        return (ShockPacketOutgoing) hPacket;
    }

    public static String getRawMessage(ShockPacket packet) {
        int originalReadIndex = packet.getReadIndex();
        packet.resetReadIndex();
        final byte[] bytes = packet.readBytes(packet.getBytesLength() - packet.getReadIndex());
        packet.setReadIndex(originalReadIndex);
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    public static void appendRawMessage(String rawMessage, ShockPacket packet) {
        packet.appendBytes(rawMessage.getBytes(StandardCharsets.ISO_8859_1));
    }
}

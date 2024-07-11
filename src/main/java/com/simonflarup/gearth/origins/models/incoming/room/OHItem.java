package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfo;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@ToString
public class OHItem {
    private final int id;
    private final String className;
    private final String owner;
    private final int wallX;
    private final int wallY;
    private final int localX;
    private final int localY;
    private final boolean rightWall;
    private final String posterId;

    public OHItem(String[] packetString) {
        this.id = Integer.parseInt(packetString[0]);
        this.className = packetString[1];
        this.owner = packetString[2];

        Pattern pattern = Pattern.compile(":w=(-?\\d+),(-?\\d+) l=(-?\\d+),(-?\\d+) ([lr])");
        Matcher matcher = pattern.matcher(packetString[3]);

        if (!matcher.find()) {
            throw new IllegalStateException("Unable to extract item position data from packet");
        }

        wallX = Integer.parseInt(matcher.group(1));
        wallY = Integer.parseInt(matcher.group(2));
        localX = Integer.parseInt(matcher.group(3));
        localY = Integer.parseInt(matcher.group(4));
        rightWall = matcher.group(5).equals("r");

        this.posterId = packetString[4];
        log.debug(this.toString());
    }

    public OHItem(ShockPacketIncoming packet) {
        this(parsePacket(packet));
    }

    private static String[] parsePacket(ShockPacketIncoming packet) {
        String input = packet.readString();
        if (input.isEmpty()) {
            int headerBytes = 2;
            final byte[] dataRemainder = packet.readBytes(packet.getBytesLength() - headerBytes, headerBytes);
            input = new String(dataRemainder, StandardCharsets.ISO_8859_1);
        }
        input = input.replace("\r", "");
        return input.split("\t");
    }

    public static OHItem[] parse(ShockPacketIncoming packet) {
        final byte[] dataRemainder = packet.readBytes(packet.getBytesLength() - packet.getReadIndex());
        String data = new String(dataRemainder, StandardCharsets.ISO_8859_1);
        String[] packets = data.split("\r\u0002");
        packet.resetReadIndex();
        int i;
        OHItem[] entities = new OHItem[(packets.length)];

        for(i = 0; i < packets.length; i++) {
            entities[i] = new OHItem(packet);
        }

        return entities;
    }

    public String getFullClassName() {
        return String.format("%s_%s", className, posterId);
    }

    public OHClientPacket getItemAddPacket() {
        return (packetInfoManager) -> {
            PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "ITEMS_2");
            ShockPacketIncoming packet = getPacket(packetInfo.getHeaderId());
            return new ShockPacketIncoming(packet);
        };
    }

    public OHClientPacket getActiveObjectUpdatePacket() {
        return (packetInfoManager) -> {
            PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "UPDATEITEM");
            ShockPacketIncoming packet = getPacket(packetInfo.getHeaderId());
            return new ShockPacketIncoming(packet);
        };
    }

    public OHClientPacket getActiveObjectRemovePacket() {
        return (packetInfoManager) -> {
            PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "REMOVEITEM");
            ShockPacketIncoming packet = new ShockPacketIncoming(packetInfo.getHeaderId());
            packet.appendString(String.valueOf(this.id));

            return new ShockPacketIncoming(packet);
        };
    }

    private ShockPacketIncoming getPacket(int headerId) {
        ShockPacketIncoming packet = new ShockPacketIncoming(headerId);

        packet.appendString(String.valueOf(this.id))
                .appendByte((byte) 9)
                .appendString(this.className)
                .appendByte((byte) 9)
                .appendString(this.owner)
                .appendByte((byte) 9)
                .appendString(String.format(":w=%d,%d l=%d,%d %s", wallX, wallY, localX, localY, rightWall ? "r" : "l"))
                .appendByte((byte) 9)
                .appendString(this.posterId);

        return packet;
    }
}

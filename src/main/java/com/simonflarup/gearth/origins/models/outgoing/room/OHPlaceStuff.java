package com.simonflarup.gearth.origins.models.outgoing.room;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;

@Getter
@ToString
public abstract class OHPlaceStuff implements OHServerPacket {
    private final int itemId;

    public OHPlaceStuff(int itemId) {
        this.itemId = itemId;
    }

    public static OHPlaceStuff parse(ShockPacketOutgoing packet) {
        String rawMessage = packet.toString();
        if (rawMessage.contains(":w=") && rawMessage.contains("l=")) {
            return new Item(packet);
        } else {
            return new Stuff(packet);
        }
    }

    private static String[] getSplitMessage(ShockPacketOutgoing packet) {
        String rawMessage = packet.toString();
        rawMessage = rawMessage.substring(2);
        return rawMessage.split(" ");
    }

    @Getter
    @ToString(callSuper = true)
    public static class Stuff extends OHPlaceStuff {
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final int z;

        public Stuff(ShockPacketOutgoing packet) {
            super(Integer.parseInt(getSplitMessage(packet)[0]));
            String[] split = getSplitMessage(packet);
            this.x = Integer.parseInt(split[1]);
            this.y = Integer.parseInt(split[2]);
            this.width = Integer.parseInt(split[3]);
            this.height = Integer.parseInt(split[4]);
            this.z = Integer.parseInt(split[5]);
        }

        public Stuff(int itemId, int x, int y, int width, int height, int z) {
            super(itemId);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.z = z;
        }

        @Override
        public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
            int headerId = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, "PLACESTUFF").getHeaderId();
            ShockPacketOutgoing packet = new ShockPacketOutgoing(headerId);
            String rawMessage = String.format("%d %d %d %d %d %d", this.getItemId(), this.x, this.y, this.width, this.height, this.z);
            packet.appendBytes(rawMessage.getBytes(StandardCharsets.ISO_8859_1));
            return packet;
        }
    }

    @Getter
    @ToString(callSuper = true)
    public static class Item extends OHPlaceStuff {
        private final int wallX;
        private final int wallY;
        private final int localX;
        private final int localY;
        private final boolean rightWall;

        public Item(ShockPacketOutgoing packet) {
            super(Integer.parseInt(getSplitMessage(packet)[0]));
            String[] split = getSplitMessage(packet);

            String[] wallCoordinates = split[1].replace(":w=", "").split(",");
            this.wallX = Integer.parseInt(wallCoordinates[0]);
            this.wallY = Integer.parseInt(wallCoordinates[1]);
            String[] localCoordinates = split[2].replace("l=", "").split(",");
            this.localX = Integer.parseInt(localCoordinates[0]);
            this.localY = Integer.parseInt(localCoordinates[1]);
            this.rightWall = split[3].equals("r");
        }

        public Item(int itemId, int wallX, int wallY, int localX, int localY, boolean rightWall) {
            super(itemId);
            this.wallX = wallX;
            this.wallY = wallY;
            this.localX = localX;
            this.localY = localY;
            this.rightWall = rightWall;
        }

        @Override
        public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
            int headerId = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, "PLACESTUFF").getHeaderId();
            ShockPacketOutgoing packet = new ShockPacketOutgoing(headerId);
            String rawMessage = String.format("%d :w=%d,%d l=%d,%d %s", this.getItemId(), this.wallX, this.wallY, this.localX, this.localY, this.rightWall ? "r" : "l");
            packet.appendBytes(rawMessage.getBytes(StandardCharsets.ISO_8859_1));
            return packet;
        }
    }
}

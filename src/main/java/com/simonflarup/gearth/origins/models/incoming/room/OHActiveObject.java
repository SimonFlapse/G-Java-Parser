package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacket;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class OHActiveObject {
    private final int id;
    private final String className;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final OHObjectDirection direction;
    private final double z;
    private final String colors;
    private final String runtimeData;
    private final int extra;
    @Setter
    private String stuffData;

    public OHActiveObject(ShockPacket packet) {
        this.id = Integer.parseInt(packet.readString());
        this.className = packet.readString();
        this.x = packet.readInteger();
        this.y = packet.readInteger();
        this.width = packet.readInteger();
        this.height = packet.readInteger();
        this.direction = OHObjectDirection.fromValue(packet.readInteger());
        this.z = Double.parseDouble(packet.readString());
        this.colors = packet.readString();
        this.runtimeData = packet.readString();
        this.extra = packet.readInteger();
        this.stuffData = packet.readString();
    }

    public static OHActiveObject[] parse(ShockPacket packet) {
        int size = packet.readInteger();

        OHActiveObject[] entities = new OHActiveObject[size];

        for(int i = 0; i < entities.length; ++i) {
            entities[i] = new OHActiveObject(packet);
        }

        return entities;
    }

    public OHClientPacket getActiveObjectAddPacket() {
        return (packetInfoManager) -> {
            PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "ACTIVEOBJECT_ADD");
            ShockPacketIncoming packet = getPacket(packetInfo.getHeaderId());
            return new ShockPacketIncoming(packet);
        };
    }

    public OHClientPacket getActiveObjectUpdatePacket() {
        return (packetInfoManager) -> {
            PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "ACTIVEOBJECT_UPDATE");
            ShockPacketIncoming packet = getPacket(packetInfo.getHeaderId());
            return new ShockPacketIncoming(packet);
        };
    }

    public OHClientPacket getActiveObjectRemovePacket() {
        return (packetInfoManager) -> {
            PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "ACTIVEOBJECT_REMOVE");
            ShockPacketIncoming packet = getPacket(packetInfo.getHeaderId());
            return new ShockPacketIncoming(packet);
        };
    }

    private ShockPacketIncoming getPacket(int headerId) {
        ShockPacketIncoming packet = new ShockPacketIncoming(headerId);

        packet.appendString(String.valueOf(this.id))
                .appendString(this.className)
                .appendInt(this.x)
                .appendInt(this.y)
                .appendInt(this.width)
                .appendInt(this.height)
                .appendInt(this.direction.getValue())
                .appendString(String.valueOf(this.z))
                .appendString(this.colors)
                .appendString(this.runtimeData)
                .appendInt(this.extra)
                .appendString(this.stuffData);

        return packet;
    }
}

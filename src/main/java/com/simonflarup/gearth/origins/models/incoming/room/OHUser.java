package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.incoming.OHBinaryGender;
import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = {"rawFigureData", "rawPoolFigureData"})
public class OHUser implements OHClientPacket {
    private final int userRoomId;
    private final String userName;
    @Getter(AccessLevel.PACKAGE)
    private final String rawFigureData;
    private final OHBinaryGender binaryGender;
    private final String motto;
    private final int x;
    private final int y;
    private final float z;
    @Getter(AccessLevel.NONE)
    private final String rawPoolFigureData;
    private final String badgeCode;
    private final UserType userType;

    public OHUser(ShockPacketIncoming packet) {
        this.userRoomId = packet.readInteger();
        this.userName = packet.readString();
        this.rawFigureData = packet.readString();
        this.binaryGender = packet.readString().equalsIgnoreCase("f") ? OHBinaryGender.FEMALE : OHBinaryGender.MALE;
        this.motto = packet.readString();
        this.x = packet.readInteger();
        this.y = packet.readInteger();
        this.z = Float.parseFloat(packet.readString());
        this.rawPoolFigureData = packet.readString();
        this.badgeCode = packet.readString();
        this.userType = UserType.fromId(packet.readInteger());
    }

    public OHUser(int userRoomId, String userName, String rawFigureData, OHBinaryGender binaryGender, String motto, int x, int y, float z, String badgeCode, UserType userType) {
        this.userRoomId = userRoomId;
        this.userName = userName;
        this.rawFigureData = rawFigureData;
        this.binaryGender = binaryGender;
        this.motto = motto;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rawPoolFigureData = "";
        this.badgeCode = badgeCode;
        this.userType = userType;
    }

    public static OHUser[] parse(ShockPacketIncoming packet) {
        int totalUsers = packet.readInteger();

        OHUser[] users = new OHUser[totalUsers];

        for (int i = 0; i < totalUsers; i++) {
            users[i] = new OHUser(packet);
        }

        return users;
    }

    public static ShockPacketIncoming getUserListPacket(PacketInfoManager packetInfoManager, OHUser[] users) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "USERS");
        ShockPacketIncoming packet = new ShockPacketIncoming(packetInfo.getHeaderId());
        packet.appendInt(users.length);
        for (OHUser user : users) {
            appendToPacket(packet, user);
        }
        return packet;
    }

    private static ShockPacketIncoming serializeToPacket(PacketInfoManager packetInfoManager, OHUser[] users) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "USERS");
        ShockPacketIncoming packet = new ShockPacketIncoming(packetInfo.getHeaderId());
        packet.appendInt(users.length);
        for (OHUser user : users) {
            appendToPacket(packet, user);
        }
        return packet;
    }

    private static void appendToPacket(ShockPacketIncoming packet, OHUser user) {
        packet.appendInt(user.userRoomId);
        packet.appendString(user.userName);
        packet.appendString(user.rawFigureData);
        packet.appendString(user.binaryGender == OHBinaryGender.FEMALE ? "f" : "m");
        packet.appendString(user.motto);
        packet.appendInt(user.x);
        packet.appendInt(user.y);
        packet.appendString(String.valueOf(user.z));
        packet.appendString(user.rawPoolFigureData);
        packet.appendString(user.badgeCode);
        packet.appendInt(user.userType.getId());
    }

    @Override
    public ShockPacketIncoming getIncomingPacket(PacketInfoManager packetInfoManager) {
        return serializeToPacket(packetInfoManager, new OHUser[]{this});
    }

    @Getter
    public enum UserType {
        PELLE(0),
        USER(1),
        PET(2),
        BOT(3);

        @Getter(AccessLevel.PACKAGE)
        private final int id;

        UserType(int id) {
            this.id = id;
        }

        static UserType fromId(int id) {
            for (UserType userType : UserType.values()) {
                if (userType.id == id) {
                    return userType;
                }
            }
            return null;
        }
    }

}

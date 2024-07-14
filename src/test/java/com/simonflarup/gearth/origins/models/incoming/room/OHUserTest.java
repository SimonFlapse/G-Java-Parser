package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.incoming.OHBinaryGender;
import gearth.encoding.VL64Encoding;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class OHUserTest {
    private static final String USERS_HEADER = "@A";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOCLIENT, 1, "", "USERS", "", ""));
        packetInfoManager = new PacketInfoManager(packetInfos);
    }


    private OHUser[] getUserList() {
        OHUser[] users = new OHUser[4];
        users[0] = new OHUser(1, "user1", "figure1", OHBinaryGender.FEMALE, "motto1", 1, 2, 3.0f, "badge1", OHUser.UserType.USER);
        users[1] = new OHUser(2, "user2", "figure2", OHBinaryGender.MALE, "motto2", 4, 5, 6.0f, "badge2", OHUser.UserType.PET);
        users[2] = new OHUser(2, "user3", "figure3", OHBinaryGender.MALE, "motto3", 7, 8, 9.0f, "badge2", OHUser.UserType.PELLE);
        users[3] = new OHUser(2, "user4", "figure4", OHBinaryGender.MALE, "motto4", 10, 11, 12.0f, "badge2", OHUser.UserType.BOT);

        return users;
    }

    @Nested
    class getIncomingPacket {
        private ShockPacketIncoming packet;

        @AfterEach
        void tearDown() {
            log.debug("Constructed packet: {}", packet);
        }

        @Test
        void testIncomingPacket() {
            OHUser[] users = getUserList();
            packet = OHUser.getUserListPacket(packetInfoManager, users);

            StringBuilder usersInfo = new StringBuilder();

            for (OHUser user : users) {
                usersInfo.append(getExpectedPacketStringForUser(user));
            }

            String length = new String(VL64Encoding.encode(users.length), StandardCharsets.ISO_8859_1);
            String expectedPacket = String.format("%s%s%s", USERS_HEADER, length, usersInfo);

            assertEquals(expectedPacket, packet.toString());
        }

        private String getExpectedPacketStringForUser(OHUser user) {
            String vl64_id = new String(VL64Encoding.encode(user.getUserRoomId()), StandardCharsets.ISO_8859_1);
            String binaryGenderLetter = user.getBinaryGender() == OHBinaryGender.FEMALE ? "f" : "m";
            String vl64_x = new String(VL64Encoding.encode(user.getX()), StandardCharsets.ISO_8859_1);
            String vl64_y = new String(VL64Encoding.encode(user.getY()), StandardCharsets.ISO_8859_1);
            String zString = String.format("%.1f", user.getZ());
            zString = zString.replace(",", ".");
            String vl64_userType = new String(VL64Encoding.encode(user.getUserType().getId()), StandardCharsets.ISO_8859_1);
            return String.format("%s%s[2]%s[2]%s[2]%s[2]%s%s%s[2]%s[2]%s[2]%s", vl64_id, user.getUserName(), user.getRawFigureData(), binaryGenderLetter, user.getMotto(), vl64_x, vl64_y, zString, "", user.getBadgeCode(), vl64_userType);
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacket() {
            ShockPacketIncoming packet = OHUser.getUserListPacket(packetInfoManager, getUserList());

            OHUser[] users = OHUser.parse(packet);

            assertEquals(4, users.length);

            for (int i = 0; i < users.length; i++) {
                OHUser user = users[i];
                OHUser expectedUser = getUserList()[i];
                assertEquals(expectedUser.getUserRoomId(), user.getUserRoomId());
                assertEquals(expectedUser.getUserName(), user.getUserName());
                assertEquals(expectedUser.getRawFigureData(), user.getRawFigureData());
                assertEquals(expectedUser.getBinaryGender(), user.getBinaryGender());
                assertEquals(expectedUser.getMotto(), user.getMotto());
                assertEquals(expectedUser.getX(), user.getX());
                assertEquals(expectedUser.getY(), user.getY());
                assertEquals(expectedUser.getZ(), user.getZ());
                assertEquals(expectedUser.getBadgeCode(), user.getBadgeCode());
                assertEquals(expectedUser.getUserType(), user.getUserType());
            }
        }
    }
}
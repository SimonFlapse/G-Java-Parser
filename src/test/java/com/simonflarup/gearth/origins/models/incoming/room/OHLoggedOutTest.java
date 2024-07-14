package com.simonflarup.gearth.origins.models.incoming.room;

import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class OHLoggedOutTest {
    private static final String LOGOUT_HEADER = "@A";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOCLIENT, 1, "", "LOGOUT", "", ""));
        packetInfoManager = new PacketInfoManager(packetInfos);
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
            int userRoomId = 42;
            packet = new OHLoggedOut(userRoomId).getIncomingPacket(packetInfoManager);

            String expectedPacket = String.format("%s%d", LOGOUT_HEADER, userRoomId);

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacket() {
            int userRoomId = 42;
            ShockPacketIncoming packet = new OHLoggedOut(userRoomId).getIncomingPacket(packetInfoManager);

            OHLoggedOut loggedOut = new OHLoggedOut(packet);
            assertEquals(42, loggedOut.getUserRoomId());
        }
    }
}
package com.simonflarup.gearth.origins.models.outgoing.room;

import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class OHPlaceStuffTest {
    private static final String PLACESTUFF_HEADER = "@A";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 1, "", "PLACESTUFF", "", ""));
        packetInfoManager = new PacketInfoManager(packetInfos);
    }

    @Nested
    class getOutgoingPacket {
        private ShockPacketOutgoing packet;

        @AfterEach
        void tearDown() {
            log.debug("Constructed packet: {}", packet);
        }

        @Test
        void forStuff() {
            OHPlaceStuff.Stuff placeStuff = new OHPlaceStuff.Stuff(42, 1, 2, 3, 4, 10);
            packet = placeStuff.getOutgoingPacket(packetInfoManager);

            String expectedPacket = String.format("%s%s", PLACESTUFF_HEADER, "42 1 2 3 4 10");

            assertEquals(expectedPacket, packet.toString());
        }

        @Test
        void forItem() {
            OHPlaceStuff.Item placeStuff = new OHPlaceStuff.Item(42, 1, 2, 3, 4, false);
            packet = placeStuff.getOutgoingPacket(packetInfoManager);

            String expectedPacket = String.format("%s%d :w=%d,%d l=%d,%d %s", PLACESTUFF_HEADER, 42, 1, 2, 3, 4, "l");

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesStuffPacket() {
            OHPlaceStuff.Stuff placeStuff = new OHPlaceStuff.Stuff(getPlaceStuffStuffPacket());
            assertEquals(42, placeStuff.getItemId());
            assertEquals(1, placeStuff.getX());
            assertEquals(2, placeStuff.getY());
            assertEquals(3, placeStuff.getWidth());
            assertEquals(4, placeStuff.getHeight());
            assertEquals(10, placeStuff.getZ());
        }

        @Test
        void parsesItemPacket() {
            OHPlaceStuff.Item placeStuff = new OHPlaceStuff.Item(getPlaceStuffItemPacket());
            assertEquals(42, placeStuff.getItemId());
            assertEquals(1, placeStuff.getWallX());
            assertEquals(2, placeStuff.getWallY());
            assertEquals(3, placeStuff.getLocalX());
            assertEquals(4, placeStuff.getLocalY());
            assertFalse(placeStuff.isRightWall());
        }

        private ShockPacketOutgoing getPlaceStuffStuffPacket() {
            OHPlaceStuff.Stuff placeStuff = new OHPlaceStuff.Stuff(42, 1, 2, 3, 4, 10);
            return placeStuff.getOutgoingPacket(packetInfoManager);
        }

        private ShockPacketOutgoing getPlaceStuffItemPacket() {
            OHPlaceStuff.Item placeStuff = new OHPlaceStuff.Item(42, 1, 2, 3, 4, false);
            return placeStuff.getOutgoingPacket(packetInfoManager);
        }
    }
}
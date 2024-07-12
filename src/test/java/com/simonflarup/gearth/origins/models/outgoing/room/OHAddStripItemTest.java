package com.simonflarup.gearth.origins.models.outgoing.room;

import gearth.encoding.Base64Encoding;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
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
class OHAddStripItemTest {
    private static final String ADDSTRIPITEM_HEADER = "@A";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 1, "", "ADDSTRIPITEM", "", ""));
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
            int itemId = 139419;
            OHStripItemType type = OHStripItemType.STUFF;
            assertOutgoingPacket(itemId, type);
        }

        @Test
        void forItem() {
            int itemId = 139420;
            OHStripItemType type = OHStripItemType.ITEM;
            assertOutgoingPacket(itemId, type);
        }

        private void assertOutgoingPacket(int itemId, OHStripItemType type) {
            OHAddStripItem addStripItem = new OHAddStripItem(itemId, type);
            packet = addStripItem.getOutgoingPacket(packetInfoManager);

            String expectedMessage = String.format("new %s %d", type.name().toLowerCase(), itemId);
            String lengthEncoding = new String(Base64Encoding.encode(expectedMessage.length(), 2), StandardCharsets.ISO_8859_1);
            String expectedPacket = String.format("%s%snew %s %d", ADDSTRIPITEM_HEADER, lengthEncoding, type.name().toLowerCase(), itemId);

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacketForStuff() {
            assertShockPacketParsing(139421, OHStripItemType.STUFF);
        }

        @Test
        void parsesShockPacketForItem() {
            assertShockPacketParsing(139422, OHStripItemType.ITEM);
        }

        private void assertShockPacketParsing(int itemId, OHStripItemType type) {
            OHAddStripItem addStripItem = new OHAddStripItem(getAddStripItemOutPackage(itemId, type));

            assertEquals(itemId, addStripItem.getItemId());
            assertEquals(type, addStripItem.getType());
        }

        private ShockPacketOutgoing getAddStripItemOutPackage(int itemId, OHStripItemType type) {
            OHAddStripItem addStripItem = new OHAddStripItem(itemId, type);
            return addStripItem.getOutgoingPacket(packetInfoManager);
        }
    }

    ;
}
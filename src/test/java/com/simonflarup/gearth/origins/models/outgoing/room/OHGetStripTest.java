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

@Slf4j
class OHGetStripTest {
    private static final String GETSTRIP_HEADER = "@A";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 1, "", "GETSTRIP", "", ""));
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
        void forNewCommand() {
            assertOutgoingPacket(OHGetStrip.OHGetStripCommand.NEW);
        }

        @Test
        void forNextCommand() {
            assertOutgoingPacket(OHGetStrip.OHGetStripCommand.NEXT);
        }

        @Test
        void forPreviousCommand() {
            assertOutgoingPacket(OHGetStrip.OHGetStripCommand.PREVIOUS);
        }

        @Test
        void forUpdateCommand() {
            assertOutgoingPacket(OHGetStrip.OHGetStripCommand.UPDATE);
        }

        private void assertOutgoingPacket(OHGetStrip.OHGetStripCommand command) {
            OHGetStrip getStrip = new OHGetStrip(command);
            packet = getStrip.getOutgoingPacket(packetInfoManager);

            String expectedPacket = String.format("%s%s", GETSTRIP_HEADER, command.getCommand());

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacketForNewCommand() {
            assertShockPacketParsing(OHGetStrip.OHGetStripCommand.NEW);
        }

        @Test
        void parsesShockPacketForNextCommand() {
            assertShockPacketParsing(OHGetStrip.OHGetStripCommand.NEXT);
        }

        @Test
        void parsesShockPacketForPreviousCommand() {
            assertShockPacketParsing(OHGetStrip.OHGetStripCommand.PREVIOUS);
        }

        @Test
        void parsesShockPacketForUpdateCommand() {
            assertShockPacketParsing(OHGetStrip.OHGetStripCommand.UPDATE);
        }

        private void assertShockPacketParsing(OHGetStrip.OHGetStripCommand command) {
            OHGetStrip getStrip = new OHGetStrip(getGetStripPackage(command));

            assertEquals(command, getStrip.getCommand());
        }

        private ShockPacketOutgoing getGetStripPackage(OHGetStrip.OHGetStripCommand command) {
            OHGetStrip getStrip = new OHGetStrip(command);
            return getStrip.getOutgoingPacket(packetInfoManager);
        }
    }
}
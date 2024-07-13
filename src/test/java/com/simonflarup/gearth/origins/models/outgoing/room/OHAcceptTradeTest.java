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
class OHAcceptTradeTest {
    private static final String TRADE_ACCEPT_HEADER = "@A";
    private static final String TRADE_UNACCEPT_HEADER = "@B";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 1, "", "TRADE_ACCEPT", "", ""));
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 2, "", "TRADE_UNACCEPT", "", ""));
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
        void forAcceptCommand() {
            assertOutgoingPacket(OHAcceptTrade.OHAcceptTradeType.ACCEPT);
        }

        @Test
        void forUnacceptCommand() {
            assertOutgoingPacket(OHAcceptTrade.OHAcceptTradeType.UNACCEPT);
        }


        void assertOutgoingPacket(OHAcceptTrade.OHAcceptTradeType type) {
            OHAcceptTrade acceptTrade = new OHAcceptTrade(type);
            packet = acceptTrade.getOutgoingPacket(packetInfoManager);

            String expectedPacket = String.format("%s", type == OHAcceptTrade.OHAcceptTradeType.ACCEPT ? TRADE_ACCEPT_HEADER : TRADE_UNACCEPT_HEADER);

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacketForAcceptcommand() {
            assertShockPacketParsing(OHAcceptTrade.OHAcceptTradeType.ACCEPT);
        }

        @Test
        void parsesShockPacketForUnacceptcommand() {
            assertShockPacketParsing(OHAcceptTrade.OHAcceptTradeType.UNACCEPT);
        }

        void assertShockPacketParsing(OHAcceptTrade.OHAcceptTradeType type) {
            ShockPacketOutgoing packet = new OHAcceptTrade(type).getOutgoingPacket(packetInfoManager);
            OHAcceptTrade acceptTrade = new OHAcceptTrade(packet, packetInfoManager);

            assertEquals(type, acceptTrade.getType());
        }
    }
}
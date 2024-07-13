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
class OHAddTradeItemTest {
    private static final String TRADE_ADDITEM_HEADER = "@A";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 1, "", "TRADE_ADDITEM", "", ""));
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
        void assertOutgoingPacket() {
            int itemId = 43921;
            OHAddTradeItem addTradeItem = new OHAddTradeItem(itemId);
            packet = addTradeItem.getOutgoingPacket(packetInfoManager);

            String expectedPacket = String.format("%s%d", TRADE_ADDITEM_HEADER, itemId);

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacket() {
            int itemId = 98241;
            ShockPacketOutgoing packet = new OHAddTradeItem(itemId).getOutgoingPacket(packetInfoManager);
            OHAddTradeItem addTradeItem = new OHAddTradeItem(packet);

            assertEquals(itemId, addTradeItem.getItemId());
        }
    }
}
package com.simonflarup.gearth.origins.models.outgoing.registrat;

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
class OHInfoRetrieveTest {
    private static final String INFORETRIEVE_HEADER = "@A";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 1, "", "INFORETRIEVE", "", ""));
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
            OHInfoRetrieve infoRetrieve = new OHInfoRetrieve();
            packet = infoRetrieve.getOutgoingPacket(packetInfoManager);

            String expectedPacket = String.format("%s", INFORETRIEVE_HEADER);

            assertEquals(expectedPacket, packet.toString());
        }
    }
}
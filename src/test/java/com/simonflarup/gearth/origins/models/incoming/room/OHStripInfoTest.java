package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.outgoing.room.OHStripItemType;
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
class OHStripInfoTest {
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOCLIENT, 1, "", "STRIPINFO_2", "", ""));
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
            OHStripInfo[] stripInfoArray = new OHStripInfo[2];
            stripInfoArray[0] = new OHStripStuff(-42, 0, 42, "teddy", 1, 1, "#ffffff,#c38d1a,#ffffff,#c38d1a");
            stripInfoArray[1] = new OHStripItem(-43, 1, 43, "poster", "10");
            OHStripInfo.OHStripInfos stripInfos = new OHStripInfo.OHStripInfos(stripInfoArray, 3);
            packet = stripInfos.getIncomingPacket(packetInfoManager);

            String expectedPacket = "@AJVJHS[2]RJteddy[2]II#ffffff,#c38d1a,#ffffff,#c38d1a[2]WJII[2]SJposter[2]10[2]K";

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacket() {
            OHStripInfo.OHStripInfos stripInfos = OHStripInfo.parse(getStripInfo2Packet());

            assertEquals(2, stripInfos.getStripInfos().length);
            assertEquals(3, stripInfos.getTotalInStrip());

            OHStripStuff stuff = (OHStripStuff) stripInfos.getStripInfos()[0];
            assertEquals(OHStripItemType.STUFF, stuff.getType());

            OHStripItem item = (OHStripItem) stripInfos.getStripInfos()[1];
            assertEquals(OHStripItemType.ITEM, item.getType());
        }

        private ShockPacketIncoming getStripInfo2Packet() {
            OHStripInfo[] stripInfoArray = new OHStripInfo[2];
            stripInfoArray[0] = new OHStripStuff(-42, 0, 42, "teddy", 1, 1, "#ffffff,#c38d1a,#ffffff,#c38d1a");
            stripInfoArray[1] = new OHStripItem(-43, 1, 43, "poster", "10");
            OHStripInfo.OHStripInfos stripInfos = new OHStripInfo.OHStripInfos(stripInfoArray, 3);
            return stripInfos.getIncomingPacket(packetInfoManager);
        }
    }
}
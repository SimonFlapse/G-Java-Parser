package com.simonflarup.gearth.origins.models.incoming.chat;

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
class OHChatInTest {
    private static final String SAY_HEADER = "@A";
    private static final String WHISPER_HEADER = "@B";
    private static final String SHOUT_HEADER = "@C";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOCLIENT, 1, "", OHChatInType.SAY.getIncomingHeaderName(), "", ""));
        packetInfos.add(new PacketInfo(HMessage.Direction.TOCLIENT, 2, "", OHChatInType.WHISPER.getIncomingHeaderName(), "", ""));
        packetInfos.add(new PacketInfo(HMessage.Direction.TOCLIENT, 3, "", OHChatInType.SHOUT.getIncomingHeaderName(), "", ""));
        packetInfoManager = new PacketInfoManager(packetInfos);
    }

    private ShockPacketIncoming getChatInPacket(short speakerId, String message, OHChatInType type) {
        OHChatIn chatIn = new OHChatIn(speakerId, message, type);
        return chatIn.getIncomingPacket(packetInfoManager);
    }

    @Nested
    class getIncomingPacket {
        private ShockPacketIncoming packet;

        @AfterEach
        void tearDown() {
            log.debug("Constructed packet: {}", packet);
        }

        @Test
        void saying() {
            assertIncomingPacket((short) 40, "Saying a test message", OHChatInType.SAY);
        }

        @Test
        void whispering() {
            assertIncomingPacket((short) 41, "Whispering a test message", OHChatInType.WHISPER);
        }

        @Test
        void shouting() {
            assertIncomingPacket((short) 42, "Shouting a test message", OHChatInType.SHOUT);
        }

        private void assertIncomingPacket(short speakerId, String message, OHChatInType type) {
            OHChatIn chatIn = new OHChatIn(speakerId, message, type);
            packet = chatIn.getIncomingPacket(packetInfoManager);

            String expectedHeader = type == OHChatInType.SAY ? SAY_HEADER : type == OHChatInType.WHISPER ? WHISPER_HEADER : SHOUT_HEADER;
            String expectedSpeakerId = new String(VL64Encoding.encode(speakerId), StandardCharsets.ISO_8859_1);
            String expectedPacket = String.format("%s%s%s[2]", expectedHeader, expectedSpeakerId, message);

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacketForSay() {
            assertShockPacketParsing((short) 40, "Saying a test message", OHChatInType.SAY);
        }

        @Test
        void parsesShockPacketForWhisper() {
            assertShockPacketParsing((short) 41, "Whispering a test message", OHChatInType.WHISPER);
        }

        @Test
        void parsesShockPacketForShout() {
            assertShockPacketParsing((short) 42, "Shouting a test message", OHChatInType.SHOUT);
        }

        private void assertShockPacketParsing(short speakerId, String message, OHChatInType type) {
            OHChatIn chatIn = new OHChatIn(getChatInPacket(speakerId, message, type), packetInfoManager);

            assertEquals(speakerId, chatIn.getSpeakerId());
            assertEquals(message, chatIn.getMessage());
            assertEquals(type, chatIn.getType());
        }
    }
}
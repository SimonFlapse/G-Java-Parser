package com.simonflarup.gearth.origins.models.outgoing.chat;

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
class OHChatOutTest {
    private static final String SAY_HEADER = "@A";
    private static final String WHISPER_HEADER = "@B";
    private static final String SHOUT_HEADER = "@C";
    private PacketInfoManager packetInfoManager;

    @BeforeEach
    void setUp() {
        List<PacketInfo> packetInfos = new ArrayList<>();
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 1, "", OHChatOutType.SAY.getOutgoingHeaderName(), "", ""));
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 2, "", OHChatOutType.WHISPER.getOutgoingHeaderName(), "", ""));
        packetInfos.add(new PacketInfo(HMessage.Direction.TOSERVER, 3, "", OHChatOutType.SHOUT.getOutgoingHeaderName(), "", ""));
        packetInfoManager = new PacketInfoManager(packetInfos);
    }

    private ShockPacketOutgoing getChatOutPacket(String message, OHChatOutType type) {
        OHChatOut chatOut = new OHChatOut(message, type);
        return chatOut.getOutgoingPacket(packetInfoManager);
    }

    private ShockPacketOutgoing getChatOutPacket(String message, OHChatOutType type, String targetUser) {
        OHChatOut chatOut = new OHChatOut(message, type, targetUser);
        return chatOut.getOutgoingPacket(packetInfoManager);
    }

    @Nested
    class getOutgoingPacket {
        private ShockPacketOutgoing packet;

        @AfterEach
        void tearDown() {
            log.debug("Constructed packet: {}", packet);
        }

        @Test
        void whispering() {
            String message = "Whispering a test message";
            String testUser = "TestUser";
            OHChatOut chatOut = new OHChatOut(message, OHChatOutType.WHISPER, testUser);
            packet = chatOut.getOutgoingPacket(packetInfoManager);

            String lengthEncoding = new String(Base64Encoding.encode(message.length() + testUser.length() + " ".length(), 2), StandardCharsets.ISO_8859_1);
            String expectedPacket = String.format("%s%s%s %s", WHISPER_HEADER, lengthEncoding, testUser, message);

            assertEquals(expectedPacket, packet.toString());
        }

        @Test
        void saying() {
            assertOutgoingPacket("Saying a test message", OHChatOutType.SAY);
        }

        @Test
        void whispering_no_target() {
            assertOutgoingPacket("Whispering a test message", OHChatOutType.WHISPER);
        }

        @Test
        void shouting() {
            assertOutgoingPacket("Shouting a test message", OHChatOutType.SHOUT);
        }

        private void assertOutgoingPacket(String message, OHChatOutType type) {
            OHChatOut chatOut = new OHChatOut(message, type);
            packet = chatOut.getOutgoingPacket(packetInfoManager);

            String expectedHeader = type == OHChatOutType.SAY ? SAY_HEADER : type == OHChatOutType.WHISPER ? WHISPER_HEADER : SHOUT_HEADER;

            // Whisper messages have a space separator between the target user and the message
            // the parser needs to add this space even if no target user has been set
            if (type == OHChatOutType.WHISPER) {
                message = " " + message;
            }
            String lengthEncoding = new String(Base64Encoding.encode(message.length(), 2), StandardCharsets.ISO_8859_1);
            String expectedPacket = String.format("%s%s%s", expectedHeader, lengthEncoding, message);

            assertEquals(expectedPacket, packet.toString());
        }
    }

    @Nested
    class Constructor {
        @Test
        void parsesShockPacketForWhisperWithTarget() {
            String message = "Whispering a test message";
            String testUser = "TestUser";
            OHChatOut chatOut = new OHChatOut(getChatOutPacket(message, OHChatOutType.WHISPER, testUser), packetInfoManager);

            assertEquals(message, chatOut.getMessage());
            assertEquals(OHChatOutType.WHISPER, chatOut.getType());
            assertEquals(testUser, chatOut.getTargetUser());
        }

        @Test
        void parsesShockPacketForSay() {
            assertShockPacketParsing("Saying a test message", OHChatOutType.SAY);
        }

        @Test
        void parsesShockPacketForWhisper() {
            assertShockPacketParsing("Whispering a test message", OHChatOutType.WHISPER);
        }

        @Test
        void parsesShockPacketForShout() {
            assertShockPacketParsing("Shouting a test message", OHChatOutType.SHOUT);
        }

        private void assertShockPacketParsing(String message, OHChatOutType type) {
            OHChatOut chatOut = new OHChatOut(getChatOutPacket(message, type), packetInfoManager);

            assertEquals(message, chatOut.getMessage());
            assertEquals(type, chatOut.getType());
            assertEquals("", chatOut.getTargetUser());
        }
    }
}
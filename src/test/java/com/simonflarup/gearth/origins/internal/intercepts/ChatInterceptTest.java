package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.OHExtension;
import com.simonflarup.gearth.origins.events.chat.OnChatInEvent;
import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.internal.events.EventSystem;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.models.incoming.chat.OHChatIn;
import com.simonflarup.gearth.origins.models.incoming.chat.OHChatInType;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ChatInterceptTest {
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

    @Test
    void onChatIn() {
        HMessage hMessage = mock(HMessage.class);
        when(hMessage.getPacket()).thenReturn(getChatInPacket((short) 40, "Saying a test message", OHChatInType.SAY));
        EventSystem eventSystem = mock(EventSystem.class);

        OHContext context = new OHContext() {
            @Override
            public OHExtension getExtension() {
                return null;
            }

            @Override
            public EventSystem getEventSystem() {
                return eventSystem;
            }

            @Override
            public PacketInfoManager getPacketInfoManager() {
                return packetInfoManager;
            }
        };

        OHMessageIn message = new OHMessageIn(hMessage, context);
        ChatIntercept.onChatIn(message);
        verify(eventSystem, times(1)).post(any(OnChatInEvent.class));

    }

}
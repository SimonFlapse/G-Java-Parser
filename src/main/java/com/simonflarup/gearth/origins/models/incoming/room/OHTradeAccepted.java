package com.simonflarup.gearth.origins.models.incoming.room;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHTradeAccepted {
    private final String username;
    private final OHTradeAcceptType type;

    public OHTradeAccepted(ShockPacketIncoming packet) {
        String rawMessage = packet.toString();
        rawMessage = rawMessage.substring(2);
        String[] data = rawMessage.split("/");

        this.username = data[0];
        this.type = data[1].equals("true") ? OHTradeAcceptType.ACCEPT : OHTradeAcceptType.UNACCEPT;
    }

    public OHTradeAccepted(String username, OHTradeAcceptType type) {
        this.username = username;
        this.type = type;
    }

    public enum OHTradeAcceptType {
        ACCEPT,
        UNACCEPT;
    }
}

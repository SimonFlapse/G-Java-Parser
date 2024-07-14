package com.simonflarup.gearth.origins.models.incoming.entry;

import com.simonflarup.gearth.origins.models.incoming.OHBinaryGender;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString(exclude = {"rawFigureData", "rawProfileFigureData"})
public class OHUserObject {
    private final String userName;
    @Getter(AccessLevel.PACKAGE)
    private final String rawFigureData;
    private final OHBinaryGender binaryGender;
    private final String motto;
    private final int phTickets;
    @Getter(AccessLevel.PACKAGE)
    private final String rawProfileFigureData;
    private final int photoFilm;
    private final boolean directMailEnabled;
    private final boolean online;
    private final boolean publicProfileEnabled;
    private final boolean friendRequestsEnabled;
    private final boolean offlineMessagingEnabled;

    public OHUserObject(ShockPacketIncoming packet) {
        final byte[] dataRemainder = packet.readBytes(packet.getBytesLength() - packet.getReadIndex());
        String rawMessage = new String(dataRemainder, StandardCharsets.ISO_8859_1);
        String[] dataLine = rawMessage.split("\r");
        Map<String, String> dataMap = new HashMap<>();
        for (String data : dataLine) {
            String[] splitData = data.split("=", 2);
            dataMap.put(splitData[0], splitData[1]);
        }

        this.userName = dataMap.get("name");
        this.rawFigureData = dataMap.get("figure");
        this.binaryGender = dataMap.get("sex").equalsIgnoreCase("f") ? OHBinaryGender.FEMALE : OHBinaryGender.MALE;
        this.motto = dataMap.get("customData");
        this.phTickets = Integer.parseInt(dataMap.get("ph_tickets"));
        this.rawProfileFigureData = dataMap.get("ph_figure");
        this.photoFilm = Integer.parseInt(dataMap.get("photo_film"));
        this.directMailEnabled = dataMap.get("directMail").equalsIgnoreCase("1");
        this.online = dataMap.get("onlineStatus").equalsIgnoreCase("1");
        this.publicProfileEnabled = dataMap.get("publicProfileEnabled").equalsIgnoreCase("1");
        this.friendRequestsEnabled = dataMap.get("friendRequestsEnabled").equalsIgnoreCase("1");
        this.offlineMessagingEnabled = dataMap.get("offlineMessagingEnabled").equalsIgnoreCase("1");
    }
}

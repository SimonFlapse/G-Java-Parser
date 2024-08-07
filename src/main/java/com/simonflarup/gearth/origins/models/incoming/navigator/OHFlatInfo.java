package com.simonflarup.gearth.origins.models.incoming.navigator;

import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OHFlatInfo {
    private final boolean othersCanMoveFurni;
    private final OHFlatLock flatLock;
    private final int flatId;
    private final String owner;
    private final OHFlatModels roomType;
    private final String roomName;
    private final String roomDescription;
    private final boolean showOwnerName;
    private final boolean trading;
    private final boolean alertForNoCategory;
    private final int maxVisitors;
    private final int absoluteMaxVisitors;
    //#nodeType seems to be missing from the packet but always defaults to 2

    public OHFlatInfo(ShockPacketIncoming packet) {
        othersCanMoveFurni = packet.readBoolean();
        flatLock = OHFlatLock.fromValue(packet.readInteger());
        flatId = packet.readInteger();
        owner = packet.readString();

        roomType = OHFlatModels.valueOf(packet.readString().toUpperCase());
        roomName = packet.readString();
        roomDescription = packet.readString();
        showOwnerName = packet.readBoolean();
        trading = packet.readBoolean();
        alertForNoCategory = packet.readBoolean();
        maxVisitors = packet.readInteger();
        absoluteMaxVisitors = packet.readInteger();
    }
}

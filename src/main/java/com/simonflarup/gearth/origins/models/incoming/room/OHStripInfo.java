package com.simonflarup.gearth.origins.models.incoming.room;

import com.simonflarup.gearth.origins.models.incoming.OHClientPacket;
import com.simonflarup.gearth.origins.models.outgoing.room.OHStripItemType;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;
import gearth.services.packet_info.PacketInfoManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
@AllArgsConstructor()
public abstract class OHStripInfo {
    private final int id;
    private final int position;
    private final OHStripItemType type;
    private final int itemId;
    private final String className;

    public OHStripInfo(ShockPacketIncoming packet) {
        this.id = packet.readInteger();
        this.position = packet.readInteger();
        this.type = packet.readString().equals("S") ? OHStripItemType.STUFF : OHStripItemType.ITEM;
        this.itemId = Math.abs(packet.readInteger());
        this.className = packet.readString();
    }

    public static OHStripInfos parse(ShockPacketIncoming packet) {
        int count = packet.readInteger();
        OHStripInfo[] stripInfos = new OHStripInfo[count];
        for (int i = 0; i < count; i++) {
            OHStripItemType type = getType(packet);
            OHStripInfo stripInfo;

            if (type == OHStripItemType.STUFF) {
                stripInfo = new OHStripStuff(packet);
            } else {
                stripInfo = new OHStripItem(packet);
            }

            stripInfos[i] = stripInfo;
        }
        int totalInStrip = packet.readInteger();
        return new OHStripInfos(stripInfos, totalInStrip);
    }

    protected void appendToPacket(ShockPacketIncoming packet) {
        packet.appendInt(this.id);
        packet.appendInt(this.position);
        packet.appendString(this.type == OHStripItemType.STUFF ? "S" : "I");
        packet.appendInt(this.itemId);
        packet.appendString(this.className);
    }

    private static OHStripItemType getType(ShockPacketIncoming packet) {
        int originalReadIndex = packet.getReadIndex();
        packet.readInteger(); // disregard id data
        packet.readInteger(); // disregard position data
        String typeString = packet.readString();
        // Reset back to the original read index to not mess up the rest of the packet
        packet.setReadIndex(originalReadIndex);
        if (typeString.equals("S")) {
            return OHStripItemType.STUFF;
        }
        return OHStripItemType.ITEM;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static final class OHStripInfos implements OHClientPacket {
        private final OHStripInfo[] stripInfos;
        private final int totalInStrip;

        @Override
        public ShockPacketIncoming getIncomingPacket(PacketInfoManager packetInfoManager) {
            int headerId = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOCLIENT, "STRIPINFO_2").getHeaderId();
            ShockPacketIncoming packet = new ShockPacketIncoming(headerId);
            packet.appendInt(this.stripInfos.length);
            for (OHStripInfo stripInfo : this.stripInfos) {
                stripInfo.appendToPacket(packet);
            }
            packet.appendInt(this.totalInStrip);
            return packet;
        }
    }
}

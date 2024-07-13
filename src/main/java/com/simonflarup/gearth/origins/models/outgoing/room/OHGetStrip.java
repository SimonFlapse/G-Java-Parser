package com.simonflarup.gearth.origins.models.outgoing.room;

import com.simonflarup.gearth.origins.models.outgoing.OHServerPacket;
import gearth.protocol.HMessage;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketOutgoing;
import gearth.services.packet_info.PacketInfo;
import gearth.services.packet_info.PacketInfoManager;
import lombok.Getter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;

@Getter
@ToString
public class OHGetStrip implements OHServerPacket {
    private final OHGetStripCommand command;

    public OHGetStrip(ShockPacketOutgoing packet) {
        String rawPacket = packet.toString();
        rawPacket = rawPacket.substring(2);
        this.command = OHGetStripCommand.fromString(rawPacket);
    }

    public OHGetStrip(OHGetStripCommand command) {
        this.command = command;
    }

    @Override
    public ShockPacketOutgoing getOutgoingPacket(PacketInfoManager packetInfoManager) {
        PacketInfo packetInfo = packetInfoManager.getPacketInfoFromName(HMessage.Direction.TOSERVER, "GETSTRIP");
        ShockPacketOutgoing packet = new ShockPacketOutgoing(packetInfo.getHeaderId());
        packet.appendBytes(command.getCommand().getBytes(StandardCharsets.ISO_8859_1));
        return packet;
    }

    @Getter
    public enum OHGetStripCommand {
        NEW("new"),
        NEXT("next"),
        PREVIOUS("prev"),
        UPDATE("update");

        public static OHGetStripCommand fromString(String command) {
            for (OHGetStripCommand value : values()) {
                if (value.getCommand().equals(command)) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Invalid command: " + command);
        }

        private final String command;

        OHGetStripCommand(String command) {
            this.command = command;
        }
    }
}

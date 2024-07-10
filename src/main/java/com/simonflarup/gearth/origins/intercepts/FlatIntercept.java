package com.simonflarup.gearth.origins.intercepts;

import com.simonflarup.gearth.origins.events.EventSystem;
import com.simonflarup.gearth.origins.events.type.flat.OnFlatInfoEvent;
import com.simonflarup.gearth.origins.models.incoming.navigator.OHFlatInfo;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

public class FlatIntercept extends AbstractIntercept {
    public static void onFlatInfo(ShockPacketIncoming packet) {
        OHFlatInfo flatInfo = new OHFlatInfo(packet);
        EventSystem.post((OnFlatInfoEvent) () -> flatInfo);
    }
}

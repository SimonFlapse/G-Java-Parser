package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.flat.OnFlatInfoEvent;
import com.simonflarup.gearth.origins.internal.OHContext;
import com.simonflarup.gearth.origins.models.incoming.navigator.OHFlatInfo;
import gearth.protocol.packethandler.shockwave.packets.ShockPacketIncoming;

class FlatIntercept extends AbstractIntercept {
    static void onFlatInfo(ShockPacketIncoming packet, OHContext context) {
        OHFlatInfo flatInfo = new OHFlatInfo(packet);
        context.getEventSystem().post((OnFlatInfoEvent) () -> flatInfo);
    }
}

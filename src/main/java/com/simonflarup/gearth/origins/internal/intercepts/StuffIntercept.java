package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.stuff.OnPlaceStuffEvent;
import com.simonflarup.gearth.origins.internal.packets.OHMessageOut;
import com.simonflarup.gearth.origins.models.outgoing.room.OHPlaceStuff;

public class StuffIntercept extends AbstractIntercept {
    static void onPlaceStuff(OHMessageOut message) {
        OHPlaceStuff placeStuff = OHPlaceStuff.parse(message.getPacket());
        message.getContext().getEventSystem().post(new OnPlaceStuffEventImpl(placeStuff, message));
    }

    private static class OnPlaceStuffEventImpl extends OHEventImpl<OHPlaceStuff, OHMessageOut> implements OnPlaceStuffEvent {
        public OnPlaceStuffEventImpl(OHPlaceStuff data, OHMessageOut message) {
            super(data, message);
        }
    }
}

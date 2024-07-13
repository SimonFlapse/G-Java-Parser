package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.flat.OnFlatInfoEvent;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.models.incoming.navigator.OHFlatInfo;

class FlatIntercept extends AbstractIntercept {
    static void onFlatInfo(OHMessageIn message) {
        OHFlatInfo flatInfo = new OHFlatInfo(message.getPacket());
        message.getContext().getEventSystem().post(new OnFlatInfoEventImpl(flatInfo, message));
    }

    private static class OnFlatInfoEventImpl extends OHEventImpl<OHFlatInfo, OHMessageIn> implements OnFlatInfoEvent {
        public OnFlatInfoEventImpl(OHFlatInfo data, OHMessageIn message) {
            super(data, message);
        }
    }
}

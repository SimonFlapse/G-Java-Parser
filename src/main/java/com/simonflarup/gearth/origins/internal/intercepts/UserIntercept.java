package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.user.OnProfileInfoEvent;
import com.simonflarup.gearth.origins.events.user.OnRetrieveProfileInfoEvent;
import com.simonflarup.gearth.origins.events.user.OnUserExitedRoomEvent;
import com.simonflarup.gearth.origins.events.user.OnUsersEnteredRoomEvent;
import com.simonflarup.gearth.origins.internal.packets.OHMessageIn;
import com.simonflarup.gearth.origins.internal.packets.OHMessageOut;
import com.simonflarup.gearth.origins.models.incoming.entry.OHUserObject;
import com.simonflarup.gearth.origins.models.incoming.room.OHLoggedOut;
import com.simonflarup.gearth.origins.models.incoming.room.OHUser;
import com.simonflarup.gearth.origins.models.outgoing.registrat.OHInfoRetrieve;

public class UserIntercept extends AbstractIntercept {
    static void onInfoRetrieve(OHMessageOut message) {
        OHInfoRetrieve infoRetrieve = new OHInfoRetrieve();
        message.getContext().getEventSystem().post(new OnRetrieveProfileInfoEventImpl(infoRetrieve, message));
    }

    static void onUserObj(OHMessageIn message) {
        OHUserObject userObject = new OHUserObject(message.getPacket());
        message.getContext().getEventSystem().post(new OnProfileInfoEventImpl(userObject, message));
    }

    static void onUsers(OHMessageIn message) {
        OHUser[] users = OHUser.parse(message.getPacket());
        message.getContext().getEventSystem().post(new OnUsersEnteredRoomEventImpl(users, message));
    }

    static void onLogOut(OHMessageIn message) {
        OHLoggedOut loggedOut = new OHLoggedOut(message.getPacket());
        message.getContext().getEventSystem().post(new OnUserExitedRoomEventImpl(loggedOut, message));
    }

    private static class OnRetrieveProfileInfoEventImpl extends OHEventImpl<OHInfoRetrieve, OHMessageOut> implements OnRetrieveProfileInfoEvent {
        public OnRetrieveProfileInfoEventImpl(OHInfoRetrieve data, OHMessageOut message) {
            super(data, message);
        }
    }

    private static class OnProfileInfoEventImpl extends OHEventImpl<OHUserObject, OHMessageIn> implements OnProfileInfoEvent {
        public OnProfileInfoEventImpl(OHUserObject data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnUsersEnteredRoomEventImpl extends OHEventImpl<OHUser[], OHMessageIn> implements OnUsersEnteredRoomEvent {
        public OnUsersEnteredRoomEventImpl(OHUser[] data, OHMessageIn message) {
            super(data, message);
        }
    }

    private static class OnUserExitedRoomEventImpl extends OHEventImpl<OHLoggedOut, OHMessageIn> implements OnUserExitedRoomEvent {
        public OnUserExitedRoomEventImpl(OHLoggedOut data, OHMessageIn message) {
            super(data, message);
        }
    }
}

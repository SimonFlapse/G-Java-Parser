package com.simonflarup.gearth.origins.internal.services;

import com.google.common.eventbus.Subscribe;
import com.simonflarup.gearth.origins.events.flat.OnFlatInfoEvent;
import com.simonflarup.gearth.origins.events.user.OnProfileInfoEvent;
import com.simonflarup.gearth.origins.internal.events.EventSubscriber;
import com.simonflarup.gearth.origins.models.incoming.entry.OHUserObject;
import com.simonflarup.gearth.origins.models.outgoing.registrat.OHInfoRetrieve;
import com.simonflarup.gearth.origins.services.OHPacketSender;
import com.simonflarup.gearth.origins.services.OHProfileManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class OHProfileManagerImpl implements OHProfileManager {
    private static OHProfileManagerImpl INSTANCE;

    private final OHPacketSender packetSender;
    private final AtomicBoolean isProfileUpdating = new AtomicBoolean(false);

    private OHUserObject currentUser;

    private OHProfileManagerImpl(OHPacketSender packetSender) {
        this.packetSender = packetSender;
    }

    static OHProfileManagerImpl getInstance(EventSubscriber eventSubscriber, OHPacketSender packetSender) {
        if (INSTANCE == null) {
            INSTANCE = new OHProfileManagerImpl(packetSender);
            eventSubscriber.registerPriority(INSTANCE);
        }
        return INSTANCE;
    }

    @Override
    public Optional<OHUserObject> getCurrentUser() {
        if (currentUser == null) {
            updateProfile();
            return Optional.empty();
        }
        return Optional.of(currentUser);
    }

    @Subscribe
    void updateFlatInfo(OnFlatInfoEvent event) {
        // Make sure the profile is updated whenever a user enters a room
        getCurrentUser();
    }

    @Subscribe
    void onProfileInfo(OnProfileInfoEvent profileInfo) {
        currentUser = profileInfo.get();
    }

    private void updateProfile() {
        if (isProfileUpdating.getAndSet(true)) {
            log.warn("Profile is already updating. Ignoring request.");
            return;
        }

        packetSender.scheduleToServer(new OHInfoRetrieve(), (success) -> isProfileUpdating.set(!success));
    }
}
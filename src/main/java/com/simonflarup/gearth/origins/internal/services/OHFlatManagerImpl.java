package com.simonflarup.gearth.origins.internal.services;

import com.google.common.eventbus.Subscribe;
import com.simonflarup.gearth.origins.events.activeobject.OnActiveObjectAddedEvent;
import com.simonflarup.gearth.origins.events.activeobject.OnActiveObjectRemovedEvent;
import com.simonflarup.gearth.origins.events.activeobject.OnActiveObjectUpdatedEvent;
import com.simonflarup.gearth.origins.events.activeobject.OnActiveObjectsLoadedEvent;
import com.simonflarup.gearth.origins.events.flat.OnFlatInfoEvent;
import com.simonflarup.gearth.origins.events.item.OnItemAddedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemRemovedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemUpdatedEvent;
import com.simonflarup.gearth.origins.events.item.OnItemsLoadedEvent;
import com.simonflarup.gearth.origins.events.user.OnUserExitedRoomEvent;
import com.simonflarup.gearth.origins.events.user.OnUsersEnteredRoomEvent;
import com.simonflarup.gearth.origins.internal.events.EventSubscriber;
import com.simonflarup.gearth.origins.models.incoming.navigator.OHFlatInfo;
import com.simonflarup.gearth.origins.models.incoming.room.OHActiveObject;
import com.simonflarup.gearth.origins.models.incoming.room.OHItem;
import com.simonflarup.gearth.origins.models.incoming.room.OHUser;
import com.simonflarup.gearth.origins.models.outgoing.room.OHLoadItems;
import com.simonflarup.gearth.origins.models.outgoing.room.OHLoadUsers;
import com.simonflarup.gearth.origins.models.outgoing.room.OHRoomItemType;
import com.simonflarup.gearth.origins.services.OHFlatManager;
import com.simonflarup.gearth.origins.services.OHPacketSender;
import com.simonflarup.gearth.origins.services.OHProfileManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class OHFlatManagerImpl implements OHFlatManager {
    private static OHFlatManagerImpl INSTANCE;

    private final ConcurrentMap<Integer, OHActiveObject> activeObjectsInFlat = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, OHItem> itemsInFlat = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, OHUser> usersInFlat = new ConcurrentHashMap<>();
    private final AtomicBoolean flatHasUpdated = new AtomicBoolean(false);

    private final OHProfileManager profileManager;
    private final OHPacketSender packetSender;

    @Getter
    public OHFlatInfo currentFlatInfo;

    private OHFlatManagerImpl(OHProfileManager profileManager, OHPacketSender packetSender) {
        this.profileManager = profileManager;
        this.packetSender = packetSender;
    }

    static OHFlatManagerImpl getInstance(EventSubscriber eventSubscriber, OHProfileManager profileManager, OHPacketSender packetSender) {
        if (INSTANCE == null) {
            INSTANCE = new OHFlatManagerImpl(profileManager, packetSender);
            eventSubscriber.registerPriority(INSTANCE);
        }
        return INSTANCE;
    }

    @Override
    public Map<Integer, OHActiveObject> getActiveObjectsInFlat() {
        updateState();
        return new ConcurrentHashMap<>(activeObjectsInFlat);
    }

    @Override
    public Map<Integer, OHItem> getItemsInFlat() {
        updateState();
        return new ConcurrentHashMap<>(itemsInFlat);
    }

    @Override
    public Map<Integer, OHUser> getUsersInFlat() {
        updateState();
        return new ConcurrentHashMap<>(usersInFlat);
    }

    @Subscribe
    void onItemAddedEvent(OnItemAddedEvent event) {
        addItem(event.get());
    }

    @Subscribe
    void onItemUpdatedEvent(OnItemUpdatedEvent event) {
        addItem(event.get());
    }

    private void addItem(OHItem item) {
        itemsInFlat.put(item.getId(), item);
    }

    @Subscribe
    void onItemRemovedEvent(OnItemRemovedEvent event) {
        itemsInFlat.remove(event.get());
    }

    @Subscribe
    void onItemsLoadedEvent(OnItemsLoadedEvent event) {
        itemsInFlat.clear();
        for (OHItem item : event.get()) {
            itemsInFlat.put(item.getId(), item);
        }
    }

    @Subscribe
    void onActiveObjectAddedEvent(OnActiveObjectAddedEvent event) {
        addActiveObject(event.get());
    }

    @Subscribe
    void onActiveObjectUpdatedEvent(OnActiveObjectUpdatedEvent event) {
        addActiveObject(event.get());
    }

    private void addActiveObject(OHActiveObject activeObject) {
        activeObjectsInFlat.put(activeObject.getId(), activeObject);
    }

    @Subscribe
    void onActiveObjectRemoved(OnActiveObjectRemovedEvent event) {
        OHActiveObject activeObject = event.get();
        activeObjectsInFlat.remove(activeObject.getId());
    }

    @Subscribe
    void onActiveObjectsLoaded(OnActiveObjectsLoadedEvent event) {
        activeObjectsInFlat.clear();
        for (OHActiveObject activeObject : event.get()) {
            activeObjectsInFlat.put(activeObject.getId(), activeObject);
        }
    }

    @Subscribe
    void updateFlatInfo(OnFlatInfoEvent event) {
        flatHasUpdated.set(true);
        OHFlatInfo flatInfo = event.get();
        if (currentFlatInfo == null || currentFlatInfo.getFlatId() != flatInfo.getFlatId()) {
            cleanUpAfterLeaving();
        }
        currentFlatInfo = flatInfo;
    }

    @Subscribe
    void onUsersEnteredRoom(OnUsersEnteredRoomEvent event) {
        for (OHUser user : event.get()) {
            usersInFlat.put(user.getUserRoomId(), user);
        }
    }

    @Subscribe
    void onUserExitedRoom(OnUserExitedRoomEvent event) {
        OHUser loggedOut = usersInFlat.remove(event.get().getUserRoomId());
        if (loggedOut == null) {
            return;
        }

        profileManager.getCurrentUser().ifPresent((user) -> {
            if (user.getUserName().equals(loggedOut.getUserName())) {
                cleanUpAfterLeaving();
                flatHasUpdated.set(false);
            }
        });
    }

    private void cleanUpAfterLeaving() {
        currentFlatInfo = null;
        activeObjectsInFlat.clear();
        itemsInFlat.clear();
        usersInFlat.clear();
    }

    private void updateState() {
        if (flatHasUpdated.getAndSet(true)) {
            return;
        }

        OHLoadUsers loadUsers = new OHLoadUsers();
        OHLoadItems loadWallItems = new OHLoadItems(OHRoomItemType.WALL);
        OHLoadItems loadFloorItems = new OHLoadItems(OHRoomItemType.FLOOR);
        profileManager.getCurrentUser();

        packetSender.scheduleToServer(loadUsers);
        packetSender.scheduleToServer(loadWallItems);
        packetSender.scheduleToServer(loadFloorItems);
    }
}

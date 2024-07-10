package com.simonflarup.gearth.origins.internal.events;

import com.google.common.eventbus.EventBus;
import com.simonflarup.gearth.origins.InternalExtensionProvider;

public class EventSystem implements EventPublisher, EventSubscriber {
    private final EventBus priorityEventBus = new EventBus();
    private final EventBus eventBus = new EventBus();

    public EventSystem(InternalExtensionProvider extensionProvider) {
        register(extensionProvider.getExtension());
    }

    @Override
    public void registerPriority(Object object) {
        priorityEventBus.register(object);
    }

    @Override
    public void register(Object object) {
        eventBus.register(object);
    }

    @Override
    public void post(Object event) {
        priorityEventBus.post(event);
        eventBus.post(event);
    }
}

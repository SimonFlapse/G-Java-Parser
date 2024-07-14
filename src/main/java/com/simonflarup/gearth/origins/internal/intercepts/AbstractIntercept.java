package com.simonflarup.gearth.origins.internal.intercepts;

import com.simonflarup.gearth.origins.events.OHEvent;
import com.simonflarup.gearth.origins.internal.packets.OHMessage;
import gearth.protocol.HMessage;
import lombok.ToString;

class AbstractIntercept {
    @ToString(exclude = "message")
    protected abstract static class OHEventImpl<T, U extends OHMessage> implements OHEvent<T> {
        private final T data;
        private final U message;

        public OHEventImpl(T data, U message) {
            this.data = data;
            this.message = message;
        }

        @Override
        public T get() {
            return this.data;
        }

        @Override
        public void silenceMessage() {
            this.message.setBlocked(true);
        }

        @Override
        public boolean isSilenced() {
            return this.message.isBlocked();
        }

        @Override
        public boolean isCorrupted() {
            return this.message.isCorrupted();
        }

        @Override
        public Destination getDestination() {
            return this.message.getDestination() == HMessage.Direction.TOCLIENT ? Destination.CLIENT : Destination.SERVER;
        }
    }
}

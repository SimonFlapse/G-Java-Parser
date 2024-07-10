package com.simonflarup.gearth.origins;

import lombok.Getter;

@Getter
public final class InternalExtensionProvider {
    private final OHExtension extension;

    InternalExtensionProvider(OHExtension extension) {
        this.extension = extension;
    }
}

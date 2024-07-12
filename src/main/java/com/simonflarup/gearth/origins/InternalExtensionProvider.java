package com.simonflarup.gearth.origins;

import lombok.Getter;

/**
 * For internal use only.
 */
@Getter
public final class InternalExtensionProvider {
    private final OHExtension extension;

    InternalExtensionProvider(OHExtension extension) {
        this.extension = extension;
    }
}

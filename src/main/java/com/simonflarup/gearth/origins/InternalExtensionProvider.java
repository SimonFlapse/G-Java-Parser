package com.simonflarup.gearth.origins;

import gearth.extensions.ExtensionBase;
import lombok.Getter;

/**
 * For internal use only.
 */
@Getter
public final class InternalExtensionProvider {
    private final ExtensionBase extension;

    InternalExtensionProvider(ExtensionBase extension) {
        this.extension = extension;
    }
}

package com.simonflarup.gearth.origins;

import com.simonflarup.gearth.origins.internal.Facade;
import com.simonflarup.gearth.origins.services.OHServiceProvider;
import gearth.extensions.Extension;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * <h2>Extension point for activating the G-Java-Parser library</h2>
 * <p>Extend this class instead of the {@link Extension} class to start up the G-Java-Parser library</p>
 *
 * <h3>Getting started</h3>
 * <p>
 * To get started with the G-Java-Parser library you need to extend your Extension class from this class instead of {@link Extension}.
 * <br/>
 * To subscribe to events, create a method in your extension class with the {@link com.google.common.eventbus.Subscribe} annotation
 * and with the only parameter to the method the event you want to subscribe to.
 * <h4>Example:</h4>
 * <pre>
 *         {@code
 *         @Subscribe
 *         public void onFlatInfoEvent(OnFlatInfoEvent event) {
 *             // Your code here
 *         }
 *         }
 *     </pre>
 * This gives you access to the parsed OnFlatInfoEvent that is fired when the client enters a room in Habbo Hotel: Origins.
 * <p>
 * Inspect the sub-packages of the {@link com.simonflarup.gearth.origins.events} package for all available events.
 * </p>
 * </p>
 *
 * <h3>Functionality provided</h3>
 * <p>
 * This class provides a {@link OHServiceProvider} instance that can be used
 * to interact with the Habbo Hotel: Origins client and other Services managed by G-Java-Parser.
 * <br/>
 * </p>
 *
 * <h3>Important information</h3>
 * <p>
 * If you override {@link #initExtension()} you must relay the call back to this class using {@code super();} at the top of the method.
 * <br/>
 * Otherwise the G-Java-Parser library will not be able to parse incoming and outgoing packets
 * </p>
 */
@Slf4j
public abstract class OHExtension extends Extension {
    private final Facade facade;

    /**
     * Instantiates the G-Java-Parser library
     * @param args The arguments passed to the extension from the command line
     */
    protected OHExtension(String[] args) {
        super(args);
        this.facade = Facade.getInstance(new InternalExtensionProvider(this));
    }

    /**
     * Get the {@link OHServiceProvider} to interact with the Habbo Hotel: Origins client and other common services
     * @return The {@link OHServiceProvider} managed by G-Java-Parser
     */
    public OHServiceProvider getServiceProvider() {
        return facade.getServiceProvider();
    }

    @Override
    protected void initExtension() {
        onConnect((host, port, APIVersion, versionClient, client) -> {
            if (!Objects.equals(versionClient, "SHOCKWAVE")) {
                log.error("This extension only works with Habbo Hotel: Origins");
                System.exit(0);
            }
        });

        facade.setupInterceptors();
    }
}

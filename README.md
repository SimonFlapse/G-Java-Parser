# G-Java-Parser

Library for G-Earth extensions made for Habbo Hotel: Origins

## Pre requisites

- [G-Earth v1.5.4 ](https://github.com/UnfamiliarLegacy/G-Earth/) - The packet interception tool for Habbo
- [G-Earth.jar installed in local maven repository](https://github.com/sirjonasxx/G-ExtensionStore/wiki/Native-Extension#setting-up-your-maven-environment) -
  To access G-Earth's Java API

## Getting started

To get started use this command:

```
mvn install:install-file -Dfile=G-Java-Parser-0.3.0.jar -DgroupId=com.simonflarup.gearth -DartifactId=G-Java-Parser -Dversion=0.3.0 -Dpackaging=jar
```

Alternatively you can manually install this artifact into maven similarly to how G-Earth.jar is installed (
See [mvn install-file](https://github.com/sirjonasxx/G-ExtensionStore/wiki/Native-Extension#setting-up-your-maven-environment) -
Replacing `G-Earth.jar` with `G-Java-Parser-0.3.0.jar` in the maven commands)

### Add dependency

Then add the following dependency to your `pom.xml`:

```xml

<dependencies>
    <dependency>
        <groupId>com.simonflarup.gearth</groupId>
        <artifactId>G-Java-Parser</artifactId>
      <version>0.3.0</version>
    </dependency>
</dependencies>
```

Your project should extend the `OHExtension` to activate the G-Java-Parser library.

### Full `OHExtension` example:

```java
package com.simonflarup.gearth.example;

import com.google.common.eventbus.Subscribe;
import com.simonflarup.gearth.origins.OHExtension;
import com.simonflarup.gearth.origins.events.chat.OnChatOutEvent;
import com.simonflarup.gearth.origins.events.flat.OnFlatInfoEvent;
import com.simonflarup.gearth.origins.models.outgoing.chat.OHChatOut;
import com.simonflarup.gearth.origins.models.outgoing.chat.OHChatOutType;
import com.simonflarup.gearth.origins.services.OHFlatManager;
import gearth.extensions.ExtensionInfo;
import gearth.protocol.HPacket;

/**
 * <h2>An example of a G-Earth extension for Habbo Hotel: Origins that uses the G-Java-Parser library</h2>
 * <p>Avoid manually intercepting messages like shown in the example for a regular G-Earth Extension</p>
 * <p>Instead use the {@link Subscribe} annotation to get parsed models from the G-Java-Parser library ready to use</p>
 * <p>Avoid using the {@link #sendToServer(HPacket)} or {@link #sendToClient(HPacket)} messages.
 * <p>
 * instead use {@link #getServiceProvider()} to fetch the {@link com.simonflarup.gearth.origins.services.OHPacketSender}
 * that can be used to parse and send G-Java-Parser models
 * </p>
 */
@ExtensionInfo(
        Title = "Example Extension",
        Description = "An example of a G-Earth extension for Habbo Hotel: Origins that uses the G-Java-Parser library",
        Version = "1.0.0",
        Author = "SimonFlapse"
)
public class ExampleExtension extends OHExtension {
  /**
   * Instantiates the G-Java-Parser library
   *
   * @param args The arguments passed to the extension from the command line
   */
  protected ExampleExtension(String[] args) {
    super(args);
  }

  @Subscribe
  public void onFlatInfoEvent(OnFlatInfoEvent event) {
    // This example logs the flat info event to the console
    System.out.println("Flat info event: " + event.get());
  }

  @Subscribe
  public void onChatOutEvent(OnChatOutEvent event) {
    // This example makes the user whisper all the floor items in the flat whenever they send any chat message
    // It makes use of the G-Java-Parser provided services, OHFlatManager to fetch all the floor items in the room
    // and OHPacketSender to send a preformatted whisper message to the server
    OHFlatManager flatManager = getServiceProvider().getFlatManager();
    flatManager.getActiveObjectsInFlat().forEach((objectId, object) -> {
      OHChatOut chatOut = new OHChatOut(String.format("%d: %s", objectId, object.toString()), OHChatOutType.WHISPER);
      getServiceProvider().getPacketSender().toServer(chatOut);
    });
  }

  // The main method to start the extension while developing.
  // Remember to add the `-p 9092` argument to the command line
  public static void main(String[] args) {
    new ExampleExtension(args).run();
  }
}
```

### Full `pom.xml` example:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.simonflarup.gearth</groupId>
    <artifactId>extension-example</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.simonflarup.gearth</groupId>
            <artifactId>G-Java-Parser</artifactId>
          <version>0.3.0</version>
        </dependency>
    </dependencies>
</project>
```
package dev.flxwdns.privateserver.inventory.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;

public final class WrappedComponent {

    public static AdventureComponentWrapper empty() {
        return new AdventureComponentWrapper(Component.empty());
    }

    public static AdventureComponentWrapper of(String text) {
        return new AdventureComponentWrapper(Component.text(text));
    }

    public static AdventureComponentWrapper ofMiniMessage(String text) {
        return new AdventureComponentWrapper(MiniMessage.miniMessage().deserialize(text));
    }
}

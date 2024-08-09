package dev.flxwdns.privateserver.inventory.impl;

import de.flxwdev.ascan.inventory.item.SkullCreator;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public final class ForwardItem extends PageItem {
    public ForwardItem() {
        super(true);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> pagedGui) {
        return new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWEyNmU1ZmYxODY3NzhlZWU2ZGJmOThhMTUwNzQzODRjMzIxMWQxNmJlMGYyOTQ2MGJiZDk2NGFlZmYifX19")).setDisplayName("§7Vor");
    }
}

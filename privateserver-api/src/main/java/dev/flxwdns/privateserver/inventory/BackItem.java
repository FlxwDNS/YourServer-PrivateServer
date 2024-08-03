package dev.flxwdns.privateserver.inventory;

import de.flxwdev.ascan.inventory.item.SkullCreator;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public final class BackItem extends PageItem {
    public BackItem() {
        super(false);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> pagedGui) {
        return new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg5ZGQ3YWY0YzgwM2I1Mjg3YzQzMzcwN2M3YzQzN2NjMjhkNTIxYmI2ODJjNDdhNGQzZDVkMmE0OGFmYTYifX19")).setDisplayName("§7Zurück");
    }
}

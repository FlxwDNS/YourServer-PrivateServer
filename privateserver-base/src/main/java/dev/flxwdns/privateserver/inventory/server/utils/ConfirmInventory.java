package dev.flxwdns.privateserver.inventory.server.utils;

import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class ConfirmInventory extends SingletonView {

    public ConfirmInventory(Player player, Runnable runnable) {
        super(player, Component.text("§eBestätigen"), 3, false);

        placeHolder(1);
        placeHolder(2);
        placeHolder(3);


        item(2, 1, ItemView.of(Material.ACACIA_SIGN).name("§7Bist du dir sicher§8?"));

        item(2, 7, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0=")).name("§aBestätigen"), runnable::run));

        item(2, 6, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==")).name("§cAbbrechen"), () -> {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
        }));

        player.openInventory(this.inventory());
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 2f);
    }
}

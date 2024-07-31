package dev.flxwdns.privateserver.inventory.server.plugins;

import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.inventory.server.impl.ServerInventory;
import dev.flxwdns.privateserver.user.impl.Server;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class PluginInventory extends SingletonView {

    public PluginInventory(Player player, Server server) {
        super(player, Component.text("§7Home"), 3, false);

        placeHolder(1);
        placeHolder(2);
        placeHolder(3);

        item(2, 1, new InteractItem(ItemView.of(Material.COMMAND_BLOCK).name("§eAlle Plugins"), () -> {
        }));
        item(2, 2, new InteractItem(ItemView.of(Material.BEACON).name("§eMiniGames"), () -> {
        }));

        item(2, 6, new InteractItem(ItemView.of(Material.MAP).name("§eInstalliere Plugins"), () -> {
        }));

        item(2, 7, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).name("§cZurück"), () -> {
            new ServerInventory(player, server);
        }));

        player.openInventory(this.inventory());
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }
}

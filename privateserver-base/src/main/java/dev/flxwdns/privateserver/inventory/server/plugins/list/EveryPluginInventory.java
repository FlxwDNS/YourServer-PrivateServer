package dev.flxwdns.privateserver.inventory.server.plugins.list;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import dev.flxwdns.privateserver.plugin.CustomPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public final class EveryPluginInventory extends PageableView<CustomPlugin> {

    public EveryPluginInventory(Player player) {
        super(player, Component.text("§7Plugins"), 3, false, List.of());

        for (int i = 0; i < 5; i++) {
            placeHolder(1 + i, 8);
        }
        placeHolder(6);

        open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }

    @Override
    public InteractItem constructItem(CustomPlugin plugin) {
        return null;
    }
}

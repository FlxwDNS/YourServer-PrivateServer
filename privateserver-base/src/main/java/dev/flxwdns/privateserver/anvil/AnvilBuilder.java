package dev.flxwdns.privateserver.anvil;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.function.Consumer;

public final class AnvilBuilder {

    public static void build(Player player, Consumer<String> consumer, JavaPlugin plugin) {
        var item = new ItemStack(Material.PAPER);
        var meta = item.getItemMeta();
        meta.setDisplayName("§aBestätigen");
        item.setItemMeta(meta);

        if(player == null || consumer == null || plugin == null) {
            return;
        }

        new AnvilGUI.Builder().onClick((slot, snapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return Collections.emptyList();
            }
            if (snapshot.getText().isEmpty()) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                player.closeInventory();
                return Collections.emptyList();
            }
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            Bukkit.getScheduler().runTaskLater(plugin, () -> consumer.accept(snapshot.getText()), 5L);
            return Collections.emptyList();
        }).itemLeft(new ItemStack(Material.BARRIER)).text("§6Input").interactableSlots(2).plugin(plugin).open(player);
    }
}

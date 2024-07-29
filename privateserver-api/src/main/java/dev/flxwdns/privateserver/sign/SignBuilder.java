package dev.flxwdns.privateserver.sign;

import de.rapha149.signgui.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.function.Consumer;

public final class SignBuilder {

    public static void buildSign(Player player, Consumer<String> consumer, JavaPlugin plugin) {
        SignGUI.builder()
                .setLines(null, "-------------", "Warte auf input...")
                .setHandler((unused, result) -> {
                    if (result.getLine(0).isEmpty()) {
                        player.sendMessage("§cBitte gebe einen gültigen Namen ein.");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                        return Collections.emptyList();
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> consumer.accept(result.getLine(0)), 5L);
                    return Collections.emptyList();
                }).build().open(player);
    }
}

package dev.flxwdns.privateserver.inventory.server.impl;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import de.rapha149.signgui.SignGUI;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.user.impl.Server;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public final class ServerIconInventory extends PageableView<Material> {
    private final Server server;

    public ServerIconInventory(Player player, Server server, String filter) {
        super(player, Component.text("§7Server"), 6, false, Arrays.stream(Material.values()).filter(it -> it.name().toLowerCase().startsWith(filter.toLowerCase())).toList());
        this.server = server;

        for (int i = 0; i < 5; i++) {
            placeHolder(1 + i, 8);
        }
        placeHolder(6);

        item(3, 8, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFiNjJkYjVjMGEzZmExZWY0NDFiZjcwNDRmNTExYmU1OGJlZGY5YjY3MzE4NTNlNTBjZTkwY2Q0NGZiNjkifX19")).name("§7Filter"), () -> {
            SignGUI.builder()
                    .setLines(null, "-------------", "Warte auf input...")
                    .setHandler((unused, result) -> {
                        if (result.getLine(0).isEmpty()) {
                            player.sendMessage("§cBitte gebe einen gültigen Namen ein.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                            return Collections.emptyList();
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        Bukkit.getScheduler().runTaskLater(PrivateServer.instance(), () -> new ServerIconInventory(player, server, result.getLine(0).toLowerCase()), 5L);
                        return Collections.emptyList();
                    });
        }));

        open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 2f);
    }

    @Override
    public InteractItem constructItem(Material material) {
        return new InteractItem(ItemView.of(material).name("§7Material§8: " + material.name()), () -> {
            var user = PrivateServer.instance().userHandler().user(this.player());
            this.server.icon(material);
            user.updateServer(this.server);
            PrivateServer.instance().userHandler().update(user);

            new ServerInventory(this.player(), this.server);
        });
    }
}

package dev.flxwdns.privateserver.inventory.server.impl;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.sign.SignBuilder;
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
        super(player, Component.text("§7Server Icon"), 6, false, Arrays.stream(Material.values()).filter(it -> it.toString().toLowerCase().startsWith(filter.toLowerCase()) && !it.isAir() && !it.isEmpty()).toList());
        this.server = server;

        for (int i = 0; i < 5; i++) {
            placeHolder(1 + i, 8);
        }
        placeHolder(6);

        item(3, 8, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFiNjJkYjVjMGEzZmExZWY0NDFiZjcwNDRmNTExYmU1OGJlZGY5YjY3MzE4NTNlNTBjZTkwY2Q0NGZiNjkifX19")).name("§7Filter"), () -> {
            SignBuilder.buildSign(player, name -> new ServerIconInventory(player, server, name), PrivateServer.instance());
        }));

        open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 2f);
    }

    @Override
    public InteractItem constructItem(Material material) {
        return new InteractItem(ItemView.of(material).name("§7Material§8: §e" + material), () -> {
            var user = PrivateServer.instance().userHandler().user(this.player());
            this.server.icon(material);
            user.updateServer(this.server);
            PrivateServer.instance().userHandler().update(user);

            new ServerInventory(this.player(), this.server);
        });
    }
}

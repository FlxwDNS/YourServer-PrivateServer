package dev.flxwdns.privateserver.inventory;

import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.server.ServerListInventory;
import dev.flxwdns.privateserver.inventory.server.filter.ServerFilter;
import dev.flxwdns.privateserver.inventory.subdomain.SubDomainListInventory;
import dev.flxwdns.privateserver.user.impl.Server;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class HomeInventory extends SingletonView {

    public HomeInventory(Player player) {
        super(player, Component.text("§7Home"), 3, false);

        placeHolder(1);
        placeHolder(2);
        placeHolder(3);

        item(2, 2, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19")).name("§aServer erstellen").list(List.of(
                Component.text("§7Erstelle deinen eigenen Server§8.")
        )), () -> {
            var user = PrivateServer.instance().userHandler().user(player);
            user.servers().add(new Server(UUID.randomUUID(), System.currentTimeMillis(), "Server von " + player.getName(), null, Material.SCAFFOLDING));
            PrivateServer.instance().userHandler().update(user);
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }));

        item(2, 4, new InteractItem(ItemView.of(Material.FURNACE).name("§9Server Verwaltung"), () -> {
            new ServerListInventory(player, ServerFilter.ALL);
        }));

        item(2, 6, new InteractItem(ItemView.of(Material.NAME_TAG).name("§dSubdomain Verwaltung"), () -> {
            new SubDomainListInventory(player);
        }));

        player.openInventory(this.inventory());
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }
}

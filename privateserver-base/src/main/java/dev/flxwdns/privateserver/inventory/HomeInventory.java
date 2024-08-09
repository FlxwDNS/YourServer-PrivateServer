package dev.flxwdns.privateserver.inventory;

import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.impl.WrappedComponent;
import dev.flxwdns.privateserver.inventory.server.ServerListInventory;
import dev.flxwdns.privateserver.inventory.server.filter.ServerFilter;
import dev.flxwdns.privateserver.inventory.server.utils.ConfirmInventory;
import dev.flxwdns.privateserver.inventory.subdomain.SubDomainListInventory;
import dev.flxwdns.privateserver.user.impl.Server;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.UUID;

public final class HomeInventory {

    public HomeInventory(Player player) {
        var gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# E # # V # # S #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§7 "))
                .addIngredient('E', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19"))
                        .setDisplayName("§aServer erstellen")
                        .setLore(List.of(WrappedComponent.of("§7Erstelle deinen eigenen Server§8."))), click -> {
                    new ConfirmInventory(click.getPlayer(), () -> {
                        var user = PrivateServer.instance().userHandler().user(click.getPlayer().getUniqueId());
                        user.servers().add(new Server(UUID.randomUUID(), -1, System.currentTimeMillis(), null, "Server von " + click.getPlayer().getName(), null, Material.SCAFFOLDING.name()));
                        PrivateServer.instance().userHandler().update(user);
                        click.getPlayer().closeInventory();
                        click.getPlayer().playSound(click.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    });
                }))
                .addIngredient('V', new SimpleItem(new ItemBuilder(Material.FURNACE).setDisplayName("§9Server Verwaltung"), click -> {
                    new ServerListInventory(click.getPlayer(), ServerFilter.ALL);
                }))
                .addIngredient('S', new SimpleItem(new ItemBuilder(Material.NAME_TAG).setDisplayName("§dSubdomain Verwaltung"), click -> {
                    new SubDomainListInventory(click.getPlayer());
                }));

        Window.single().setViewer(player).setTitle(WrappedComponent.of("§7Home")).setGui(gui).build().open();

        //player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }
}

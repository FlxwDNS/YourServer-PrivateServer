package dev.flxwdns.privateserver.inventory.subdomain;

import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.impl.BackItem;
import dev.flxwdns.privateserver.inventory.impl.ForwardItem;
import dev.flxwdns.privateserver.inventory.HomeInventory;
import dev.flxwdns.privateserver.inventory.impl.WrappedComponent;
import dev.flxwdns.privateserver.inventory.subdomain.create.SubDomainCreateInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.stream.Collectors;

public final class SubDomainListInventory {

    public SubDomainListInventory(Player player) {
        var gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x # R #",
                        "# x x x x x # # #",
                        "# x x x x x # # #",
                        "# x x x x x # Z #",
                        "# # < # > # # # #"
                )
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', ItemView.of(Material.GRAY_STAINED_GLASS_PANE).name("§7 "))
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .addIngredient('R', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19")).setDisplayName("§aSubdomain registrieren"), click -> {
                    new SubDomainCreateInventory(player, null, null);
                }))
                .addIngredient('Z', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).setDisplayName("§cZurück"), click -> {
                    new HomeInventory(player);
                }))
                .setContent(PrivateServer.instance().userHandler().user(player.getUniqueId()).domains()
                        .stream()
                        .map(it -> new SimpleItem(new ItemBuilder(Material.PAPER).setDisplayName("§e" + it.domain()), click -> {
                            new SubDomainInventory(player, it);
                        })).collect(Collectors.toList()))
                .build();

        Window.single().setViewer(player).setTitle(WrappedComponent.of("§dSubdomains")).setGui(gui).build().open();
    }
}

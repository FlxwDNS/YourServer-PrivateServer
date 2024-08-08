package dev.flxwdns.privateserver.inventory.subdomain;

import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.BackItem;
import dev.flxwdns.privateserver.inventory.ForwardItem;
import dev.flxwdns.privateserver.inventory.WrappedComponent;
import dev.flxwdns.privateserver.inventory.server.impl.ServerInventory;
import dev.flxwdns.privateserver.inventory.subdomain.create.SubDomainSelectInventory;
import dev.flxwdns.privateserver.user.impl.Domain;
import dev.flxwdns.privateserver.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.stream.Collectors;

public final class SubDomainSelectServerInventory {

    public SubDomainSelectServerInventory(Player player, Domain domain) {
        var gui = PagedGui.items()
                .setStructure(
                        "x x x x x x x x #",
                        "x x x x x x x x #",
                        "x x x x x x x x Z",
                        "x x x x x x x x #",
                        "x x x x x x x x #",
                        "# # # < # > # # #"
                )
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§7 "))
                .addIngredient('Z', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).setDisplayName("§cZurück"), click -> {
                    new SubDomainInventory(player, domain);
                }))
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(PrivateServer.instance().userHandler().user(player).servers()
                        .stream()
                        .map(server -> new SimpleItem(new ItemBuilder(server.icon()).setDisplayName("§7" + server.name()).setLegacyLore(List.of(
                                "§7",
                                "§f" + (server.description() == null ? "Keine Beschreibung gefunden." : server.description()),
                                "§7",
                                "§eKlick §8» §7Server auswählen"
                        )), click -> {
                            var user = PrivateServer.instance().userHandler().user(player);
                            domain.connectedServer(server.serverUniqueId());
                            user.updateDomain(domain);
                            PrivateServer.instance().userHandler().update(user);

                            new SubDomainInventory(player, domain);
                        })).collect(Collectors.toList()))
                .build();


        Window.single().setViewer(player).setTitle(WrappedComponent.of("§d" + domain.domain())).setGui(gui).build().open();
    }
}

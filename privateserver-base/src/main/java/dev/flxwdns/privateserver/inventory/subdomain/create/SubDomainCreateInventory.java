package dev.flxwdns.privateserver.inventory.subdomain.create;

import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.WrappedComponent;
import dev.flxwdns.privateserver.inventory.subdomain.SubDomainListInventory;
import dev.flxwdns.privateserver.sign.AnvilBuilder;
import dev.flxwdns.privateserver.user.impl.Domain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public final class SubDomainCreateInventory {

    public SubDomainCreateInventory(Player player, String domain, String subDomain) {
        var gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# D S # # # Z E #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§7 "))
                .addIngredient('D', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjE1MWNmZmRhZjMwMzY3MzUzMWE3NjUxYjM2NjM3Y2FkOTEyYmE0ODU2NDMxNThlNTQ4ZDU5YjJlYWQ1MDExIn19fQ"))
                        .setDisplayName("§bDomain wählen")
                        .setLore(List.of(
                                WrappedComponent.empty(),
                                WrappedComponent.of("§7Ausgewählte Domain§8» " + (domain == null ? "§cKeine" : "§e" + domain)),
                                WrappedComponent.empty(),
                                WrappedComponent.of("§7Wähle die Hauptdomain aus§8."),
                                WrappedComponent.of("§7Die Subdomain wird an diese Domain angehängt§8."),
                                WrappedComponent.empty(),
                                WrappedComponent.of("§eKlick §8» §7Domain auswählen")
                        )), click -> new SubDomainSelectInventory(player)))
                .addIngredient('S', new SimpleItem(new ItemBuilder(Material.NAME_TAG).setDisplayName("§dSubdomain wählen")
                        .setLore(List.of(
                                WrappedComponent.empty(),
                                WrappedComponent.of("§7Ausgewählte Subdomain§8. " + (subDomain == null ? "§cKeine" : "§e" + subDomain)),
                                WrappedComponent.empty(),
                                WrappedComponent.of("§7Wähle die Subdomain aus§8."),
                                WrappedComponent.of("§7Die Subdomain wird an die Domain angehängt§8."),
                                WrappedComponent.empty(),
                                WrappedComponent.of("§eKlick §8» §7Subdomain auswählen")
                        )), click -> AnvilBuilder.build(player, name -> new SubDomainCreateInventory(player, domain, name), PrivateServer.instance())))

                .addIngredient('Z', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).setDisplayName("§cZurück"), click -> {
                    new SubDomainListInventory(player);
                }));

        if(domain == null || subDomain == null) {
            gui.addIngredient('E', new SimpleItem(new ItemBuilder(Material.BARRIER).setDisplayName("§cFehlende Information"), click -> new SubDomainSelectInventory(player)));
        } else {
            gui.addIngredient('E', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19")).setDisplayName("§aErstellen")
                    .setLore(List.of(
                        WrappedComponent.empty(),
                        WrappedComponent.of("§7Klicke um den Vorgang abzuschließen§8.")
                    )), click -> {
                        if(PrivateServer.instance().userHandler().repository().query().find().stream().anyMatch(domain1 -> domain1.domains().stream().anyMatch(it -> it.domain().equalsIgnoreCase(subDomain + "." + domain)))) {
                            player.sendMessage(PrivateServer.instance().getConfig().getString("prefix") + "§cDiese Subdomain existiert bereits.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                            return;
                        }
                        var user = PrivateServer.instance().userHandler().user(player);
                        user.domains().add(new Domain(subDomain + "." + domain));
                        PrivateServer.instance().userHandler().update(user);

                        player.sendMessage(PrivateServer.instance().getConfig().getString("prefix") + "§aDie Subdomain wurde erfolgreich erstellt.");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                        player.closeInventory();
                    }));
        }

        Window.single().setViewer(player).setTitle(WrappedComponent.of("§dSubdomain")).setGui(gui.build()).build().open();
    }
}
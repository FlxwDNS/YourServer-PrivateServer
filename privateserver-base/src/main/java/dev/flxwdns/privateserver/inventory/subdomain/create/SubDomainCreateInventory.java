package dev.flxwdns.privateserver.inventory.subdomain.create;

import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.subdomain.SubDomainListInventory;
import dev.flxwdns.privateserver.sign.AnvilBuilder;
import dev.flxwdns.privateserver.user.impl.Domain;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public final class SubDomainCreateInventory extends SingletonView {

    public SubDomainCreateInventory(Player player, String domain, String subDomain) {
        super(player, Component.text("§dSubdomain"), 3, false);

        placeHolder(1);
        placeHolder(2);
        placeHolder(3);

        item(2, 1, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjE1MWNmZmRhZjMwMzY3MzUzMWE3NjUxYjM2NjM3Y2FkOTEyYmE0ODU2NDMxNThlNTQ4ZDU5YjJlYWQ1MDExIn19fQ==")).name("§bDomain wählen").list(List.of(
                Component.empty(),
                Component.text("§7Ausgewählte Domain§8» " + (domain == null ? "§cKeine" : "§e" + domain)),
                Component.empty(),
                Component.text("§7Wähle die Hauptdomain aus§8."),
                Component.text("§7Die Subdomain wird an diese Domain angehängt§8."),
                Component.empty(),
                Component.text("§eKlick §8» §7Domain auswählen")
        )), () -> new SubDomainSelectInventory(player)));

        item(2, 2, new InteractItem(ItemView.of(Material.NAME_TAG).name("§dSubdomain wählen").list(List.of(
                Component.empty(),
                Component.text("§7Ausgewählte Subdomain§8. " + (subDomain == null ? "§cKeine" : "§e" + subDomain)),
                Component.empty(),
                Component.text("§7Wähle die Subdomain aus§8."),
                Component.text("§7Die Subdomain wird an die Domain angehängt§8."),
                Component.empty(),
                Component.text("§eKlick §8» §7Subdomain auswählen")
        )), () -> AnvilBuilder.build(player, name -> new SubDomainCreateInventory(player, domain, name), PrivateServer.instance())));

        item(2, 6, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).name("§cZurück"), () -> {
            new SubDomainListInventory(player);
        }));

        if (domain == null || subDomain == null) {
            item(2, 7, new InteractItem(ItemView.of(Material.BARRIER).name("§cFehlende Information"), () -> new SubDomainSelectInventory(player)));
        } else {
            item(2, 7, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19")).name("§aErstellen").list(List.of(
                    Component.text("§7Klicke um den Vorgang abzuschließen§8.")
            )), () -> {
                if(PrivateServer.instance().userHandler().repository().query().find().stream().anyMatch(domain1 -> domain1.domains().stream().anyMatch(it -> it.domain().equalsIgnoreCase(subDomain + "." + domain)))) {
                    player.sendMessage("§cDiese Subdomain existiert bereits.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                    return;
                }
                var user = PrivateServer.instance().userHandler().user(player);
                user.domains().add(new Domain(subDomain + "." + domain));
                PrivateServer.instance().userHandler().update(user);

                player.sendMessage("§aDie Subdomain wurde erfolgreich erstellt.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                player.closeInventory();
            }));
        }

        player.openInventory(this.inventory());
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }
}

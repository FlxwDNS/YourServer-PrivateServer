package dev.flxwdns.privateserver.inventory.subdomain;

import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.subdomain.create.SubDomainSelectInventory;
import dev.flxwdns.privateserver.sign.AnvilBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class SubDomainInventory extends SingletonView {

    public SubDomainInventory(Player player, String domain, UUID server) {
        super(player, Component.text("§d" + domain), 3, false);

        placeHolder(1);
        placeHolder(2);
        placeHolder(3);

        item(2, 1, new InteractItem(ItemView.of(Material.CHEST_MINECART).name("§eSubdomain mit Server verbinden").list(List.of(
                Component.empty(),
                Component.text("§7Ausgewählter Server §8» " + (server == null ? "§cNicht ausgewählt" : "§e" + server)),
                Component.empty(),
                Component.text("§7Wähle einen Server aus,"),
                Component.text("§7um ihn mit der Subdomain zu verbinden§8."),
                Component.empty(),
                Component.text("§eKlick §8» §7Server auswählen")
        )), () -> new SubDomainSelectInventory(player)));

        item(2, 2, new InteractItem(ItemView.of(Material.ENDER_PEARL).name("§5Subdomain übertragen").list(List.of(
                Component.empty(),
                Component.text("§cSobald die Subdomain gelöscht wird§8,"),
                Component.text("§ckannst du diesen Vorgang nicht wiederrufen§8!"),
                Component.empty(),
                Component.text("§eKlick §8» §7Übertragen")
        )), () -> {
            AnvilBuilder.build(player, name -> {
                // TODO
            }, PrivateServer.instance());
        }));

        item(2, 6, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).name("§cZurück"), () -> {
            new SubDomainListInventory(player);
        }));

        item(2, 7, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=")).name("§c§lLöschen").list(List.of(
                Component.empty(),
                Component.text("§cSobald die Subdomain gelöscht wird§8,"),
                Component.text("§ckannst du diesen Vorgang nicht wiederrufen§8!"),
                Component.empty(),
                Component.text("§eKlick §8» §cLöschen")
        )), () -> {
            player.sendMessage("§aTODO");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
            player.closeInventory();
        }));

        player.openInventory(this.inventory());
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }
}

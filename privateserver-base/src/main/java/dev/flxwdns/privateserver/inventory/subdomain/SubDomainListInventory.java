package dev.flxwdns.privateserver.inventory.subdomain;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.HomeInventory;
import dev.flxwdns.privateserver.inventory.subdomain.create.SubDomainCreateInventory;
import dev.flxwdns.privateserver.user.impl.Domain;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class SubDomainListInventory extends PageableView<Domain> {

    public SubDomainListInventory(Player player) {
        super(player, Component.text("§dSubdomains"), 6, false, PrivateServer.instance().userHandler().user(player).domains());

        placeHolder(1);
        placeHolder(6);
        placeHolder(2, 0);
        placeHolder(3, 0);
        placeHolder(4, 0);
        placeHolder(5, 0);
        for (int i = 0; i < 4; i++) {
            placeHolder(2 + i, 6);
            placeHolder(2 + i, 7);
            placeHolder(2 + i, 8);
        }

        item(2, 7, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19")).name("§aSubdomain registrieren"), () -> {
            new SubDomainCreateInventory(player, null, null);
        }));

        item(5, 7, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).name("§cZurück"), () -> {
            new HomeInventory(player);
        }));

        setSlotLeft(2);
        setSlotRight(4);

        open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 2f);
    }

    @Override
    public InteractItem constructItem(Domain domain) {
        return new InteractItem(ItemView.of(Material.PAPER).name("§e" + domain.domain()), () -> {
            new SubDomainInventory(this.player(), domain.domain(), domain.connectedServer());
        });
    }
}

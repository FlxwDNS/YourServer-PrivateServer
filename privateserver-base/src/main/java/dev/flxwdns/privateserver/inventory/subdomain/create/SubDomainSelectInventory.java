package dev.flxwdns.privateserver.inventory.subdomain.create;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public final class SubDomainSelectInventory extends PageableView<String> {
    private final Player player;

    public SubDomainSelectInventory(Player player) {
        super(player, Component.text("§dSubdomain"), 6, false, PrivateServer.instance().getConfig().getStringList("domains"));
        this.player = player;

        placeHolder(1);
        for (int i = 0; i < 4; i++) {
            placeHolder(2 + i, 0);
            placeHolder(2 + i, 8);
        }
        placeHolder(6);

        open(player);
    }

    @Override
    public InteractItem constructItem(String domain) {
        return new InteractItem(ItemView.of(Material.PAPER).name("§b" + domain), () -> {
            new SubDomainCreateInventory(this.player, domain, null);
        });
    }
}

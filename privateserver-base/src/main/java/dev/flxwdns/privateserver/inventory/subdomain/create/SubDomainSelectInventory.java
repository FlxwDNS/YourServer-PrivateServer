package dev.flxwdns.privateserver.inventory.subdomain.create;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.BackItem;
import dev.flxwdns.privateserver.inventory.ForwardItem;
import dev.flxwdns.privateserver.inventory.WrappedComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.stream.Collectors;

public final class SubDomainSelectInventory {

    public SubDomainSelectInventory(Player player) {
        var gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # < # # # > # #"
                )
                .addIngredient('#', ItemView.of(Material.GRAY_STAINED_GLASS_PANE).name("§7 "))
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(PrivateServer.instance().getConfig().getStringList("domains")
                        .stream()
                        .map(it -> new SimpleItem(new ItemBuilder(Material.PAPER).setDisplayName("§b" + it), click -> {
                            new SubDomainCreateInventory(player, it, null);
                        })).collect(Collectors.toList()))
                .build();

        Window.single().setViewer(player).setTitle(WrappedComponent.of("§dSubdomain")).setGui(gui).build().open();
    }

    /*private final Player player;

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
    }*/
}

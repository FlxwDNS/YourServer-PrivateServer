package dev.flxwdns.privateserver.inventory.subdomain;

import de.flxwdev.ascan.inventory.item.ItemView;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.BackItem;
import dev.flxwdns.privateserver.inventory.ForwardItem;
import dev.flxwdns.privateserver.inventory.WrappedComponent;
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
                .setContent(PrivateServer.instance().userHandler().user(player).domains()
                        .stream()
                        .map(it -> new SimpleItem(new ItemBuilder(Material.PAPER).setDisplayName("§e" + it.domain()), click -> {
                            new SubDomainInventory(player, it);
                        })).collect(Collectors.toList()))
                .build();

        Window.single().setViewer(player).setTitle(WrappedComponent.of("§dSubdomains")).setGui(gui).build().open();
    }

    /*public SubDomainListInventory(Player player) {
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
            new SubDomainInventory(this.player(), domain);
        });
    }*/
}

package dev.flxwdns.privateserver.inventory.subdomain;

import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.WrappedComponent;
import dev.flxwdns.privateserver.inventory.server.ServerListInventory;
import dev.flxwdns.privateserver.inventory.server.filter.ServerFilter;
import dev.flxwdns.privateserver.inventory.server.utils.ConfirmInventory;
import dev.flxwdns.privateserver.inventory.subdomain.create.SubDomainSelectInventory;
import dev.flxwdns.privateserver.sign.AnvilBuilder;
import dev.flxwdns.privateserver.user.impl.Domain;
import dev.flxwdns.privateserver.user.impl.Server;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public final class SubDomainInventory {

    public SubDomainInventory(Player player, Domain domain) {
        var gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# V U # # # Z L #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§7 "))
                .addIngredient('V', new SimpleItem(new ItemBuilder(Material.CHEST_MINECART)
                        .setDisplayName("§eSubdomain mit Server verbinden")
                        .setLore(List.of(
                                WrappedComponent.empty(),
                                WrappedComponent.of("§7Ausgewählter Server §8» §cNicht ausgewählt"),
                                WrappedComponent.empty(),
                                WrappedComponent.of("§7Wähle einen Server aus,"),
                                WrappedComponent.of("§7um ihn mit der Subdomain zu verbinden§8."),
                                WrappedComponent.empty(),
                                WrappedComponent.of("§eKlick §8» §7Server auswählen")
                        )), click -> new SubDomainSelectInventory(player)))
                .addIngredient('U', new SimpleItem(new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§5Subdomain übertragen")
                        .setLore(List.of(WrappedComponent.empty(),
                                WrappedComponent.of("§cSobald die Subdomain gelöscht wird§8,"),
                                WrappedComponent.of("§ckannst du diesen Vorgang nicht wiederrufen§8!"),
                                WrappedComponent.empty(),
                                WrappedComponent.of("§eKlick §8» §7Übertragen")
                        )), click -> {
                    //TODO
                }))
                .addIngredient('Z', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).setDisplayName("§cZurück"), click -> {
                    new SubDomainListInventory(player);
                }))
                .addIngredient('L', new SimpleItem(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=")).setDisplayName("§c§lLöschen")
                        .setLore(List.of(WrappedComponent.empty(),
                                WrappedComponent.of("§cSobald die Subdomain gelöscht wird§8,"),
                                WrappedComponent.of("§ckannst du diesen Vorgang nicht wiederrufen§8!"),
                                WrappedComponent.empty(),
                                WrappedComponent.of("§eKlick §8» §cLöschen")
                        )), click -> {
                    player.sendMessage("§aTODO");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    player.closeInventory();
                }));

        Window.single().setViewer(player).setTitle(WrappedComponent.of("§d" + domain.domain())).setGui(gui).build().open();
    }

    /*public SubDomainInventory(Player player, String domain, UUID server) {
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
    }*/
}

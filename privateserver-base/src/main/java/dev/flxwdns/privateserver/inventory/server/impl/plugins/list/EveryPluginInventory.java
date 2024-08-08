package dev.flxwdns.privateserver.inventory.server.impl.plugins.list;

import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.BackItem;
import dev.flxwdns.privateserver.inventory.ForwardItem;
import dev.flxwdns.privateserver.inventory.HomeInventory;
import dev.flxwdns.privateserver.inventory.WrappedComponent;
import dev.flxwdns.privateserver.inventory.server.impl.ServerInventory;
import dev.flxwdns.privateserver.plugin.CustomPlugin;
import dev.flxwdns.privateserver.user.impl.Server;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class EveryPluginInventory {
    private List<CustomPlugin> getPlugins() {
        var config = PrivateServer.instance().getConfig();

        List<CustomPlugin> plugins = new ArrayList<>();
        for (Object s : config.getList("plugins.every")) {
            var name = s.toString().split("=")[0].replace("{", "");
            var vars = s.toString().split("=")[1].replace("[", "").replace("]", "").replace("}", "").split(",");

            var material = vars[0];
            var id = vars[1].substring(1);
            var description = vars[2].substring(1);

            plugins.add(new CustomPlugin(name, Material.valueOf(material), description, id));
        }

        return plugins;
    }

    public EveryPluginInventory(Player player, Server server) {
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
                    new ServerInventory(player, server);
                }))
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(this.getPlugins().stream().map(it -> {
                    if(it.isInstalled(server.serverUniqueId())) {
                        return new SimpleItem(new ItemBuilder(it.material()).setDisplayName("§a" + it.name()).setLegacyLore(List.of(
                                "§7",
                                "§7" + it.description(),
                                "§7",
                                "§7ID: §e" + (it.id().startsWith("https://") ? "Nicht vorhanden" : it.id()),
                                "§7",
                                "§a§lInstalliert§8."
                        )).addEnchantment(Enchantment.MENDING, 1, true).addItemFlags(ItemFlag.HIDE_ENCHANTS));
                    }
                    return new SimpleItem(new ItemBuilder(it.material()).setDisplayName("§e" + it.name()).setLegacyLore(List.of(
                            "§7",
                            "§7" + it.description(),
                            "§7",
                            "§7ID: §e" + (it.id().startsWith("https://") ? "Nicht vorhanden" : it.id()),
                            "§7",
                            "§eKlick §8» §7Download§8."
                    )), click -> {
                        new Thread(() -> it.download(server.serverUniqueId())).start();
                        new ServerInventory(player, server);
                        player.sendMessage(PrivateServer.instance().getConfig().getString("prefix") + "§aDas Plugin wurde erfolgreich heruntergeladen.");
                        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    });
                }).collect(Collectors.toList()))
                .build();

        Window.single().setViewer(player).setTitle(WrappedComponent.of("§7Plugins")).setGui(gui).build().open();
    }

    /*public EveryPluginInventory(Player player, Server server) {
        super(player, Component.text("§7Plugins"), 6, false, getPlugins());
        this.server = server;

        for (int i = 0; i < 5; i++) {
            placeHolder(1 + i, 8);
        }
        placeHolder(6);

        item(3, 8, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).name("§cZurück"), () -> {
            new PluginInventory(player, server);
        }));

        open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }

    @Override
    public InteractItem constructItem(CustomPlugin plugin) {
        if(plugin.isInstalled(this.server.serverUniqueId())) {
            return new InteractItem(ItemView.of(plugin.material()).name("§a" + plugin.name()).glow().list(List.of(
                    Component.empty(),
                    Component.text("§7" + plugin.description()),
                    Component.empty(),
                    Component.text("§7ID: §e" + (plugin.id().startsWith("https://") ? "Nicht vorhanden" : plugin.id())),
                    Component.empty(),
                    Component.text("§a§lInstalliert")
            )), () -> {});
        }

        return new InteractItem(ItemView.of(plugin.material()).name("§e" + plugin.name()).list(List.of(
                Component.empty(),
                Component.text("§7" + plugin.description()),
                Component.empty(),
                Component.text("§7ID: §e" + (plugin.id().startsWith("https://") ? "Nicht vorhanden" : plugin.id())),
                Component.empty(),
                Component.text("§eKlick §8» §7Download§8.")
        )), () -> {
            Server server = this.server;
            new Thread(() -> plugin.download(server.serverUniqueId())).start();

            new EveryPluginInventory(this.player(), this.server);
            this.player().sendMessage("§aDas Plugin wurde erfolgreich heruntergeladen.");
            this.player().playSound(this.player().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
        });
    }*/
}

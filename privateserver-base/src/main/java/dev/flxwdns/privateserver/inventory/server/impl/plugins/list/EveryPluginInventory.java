package dev.flxwdns.privateserver.inventory.server.impl.plugins.list;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.server.impl.ServerInventory;
import dev.flxwdns.privateserver.inventory.server.impl.plugins.PluginInventory;
import dev.flxwdns.privateserver.plugin.CustomPlugin;
import dev.flxwdns.privateserver.user.impl.Server;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EveryPluginInventory extends PageableView<CustomPlugin> {
    private static List<CustomPlugin> getPlugins() {
        var config = PrivateServer.instance().getConfig();

        List<CustomPlugin> plugins = new ArrayList<>();
        for (Object s : config.getList("plugins.every")) {
            var name = s.toString().split("=")[0].replace("{", "");
            var vars =  s.toString().split("=")[1].replace("[", "").replace("]", "").replace("}", "").split(",");

            var material = vars[0];
            var id = vars[1].substring(1);
            var description = vars[2].substring(1);

            plugins.add(new CustomPlugin(name, Material.valueOf(material), description, id));
        }

        return plugins;
    }
    private final Server server;

    public EveryPluginInventory(Player player, Server server) {
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
    }
}

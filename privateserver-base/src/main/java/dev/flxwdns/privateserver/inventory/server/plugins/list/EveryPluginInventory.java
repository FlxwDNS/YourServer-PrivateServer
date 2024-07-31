package dev.flxwdns.privateserver.inventory.server.plugins.list;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.plugin.CustomPlugin;
import net.kyori.adventure.text.Component;
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
            var description = vars[1];
            var id = vars[2];

            plugins.add(new CustomPlugin(name, Material.valueOf(material), description, id));
        }

        return plugins;
    }

    public EveryPluginInventory(Player player) {
        super(player, Component.text("§7Plugins"), 6, false, getPlugins());

        for (int i = 0; i < 5; i++) {
            placeHolder(1 + i, 8);
        }
        placeHolder(6);

        open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }

    @Override
    public InteractItem constructItem(CustomPlugin plugin) {
        return new InteractItem(ItemView.of(plugin.material()).name("§e" + plugin.name()).list(List.of(
                Component.empty(),
                Component.text("§7" + plugin.description()),
                Component.empty(),
                Component.text("§7ID: §e" + plugin.id()),
                Component.empty(),
                Component.text("§eKlick §8» §7Download§8.")
        )), () -> {
            plugin.download(Path.of(""));
        });
    }
}

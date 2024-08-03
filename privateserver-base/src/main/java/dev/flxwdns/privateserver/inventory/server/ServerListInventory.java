package dev.flxwdns.privateserver.inventory.server;

import de.flxwdev.ascan.inventory.PageableView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.inventory.HomeInventory;
import dev.flxwdns.privateserver.inventory.server.filter.ServerFilter;
import dev.flxwdns.privateserver.inventory.server.impl.ServerInventory;
import dev.flxwdns.privateserver.user.impl.Server;
import dev.flxwdns.privateserver.utils.NameFetcher;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public final class ServerListInventory {
    public ServerListInventory(Player player, ServerFilter filter) {
        var gui = PagedGui.items()
                .setStructure(
                        "x x x x x x x x #",
                        "x x x x x x x x F",
                        "x x x x x x x x #",
                        "x x x x x x x x Z",
                        "x x x x x x x x #",
                        "# # # < # > # # #"
                )
    }

    /*private static List<Server> getServers(ServerFilter filter, Player player) {
        if (filter.equals(ServerFilter.YOURS)) {
            return PrivateServer.instance().userHandler().user(player).servers();
        }
        List<Server> list = new ArrayList<>();
        PrivateServer.instance().userHandler().repository().query().find().stream().toList().forEach(it -> list.addAll(it.servers()));
        return list;
    }

    public ServerListInventory(Player player, ServerFilter filter) {
        super(player, Component.text("§7Server"), 6, false, getServers(filter, player));

        for (int i = 0; i < 5; i++) {
            placeHolder(1 + i, 8);
        }
        placeHolder(6);

            item(2, 8, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFiNjJkYjVjMGEzZmExZWY0NDFiZjcwNDRmNTExYmU1OGJlZGY5YjY3MzE4NTNlNTBjZTkwY2Q0NGZiNjkifX19")).name("§7Filter").list(List.of(
                Component.text("§7Online".replace("§7", filter.equals(ServerFilter.ONLINE) ? "§a" : "§7")),
                Component.text("§7Offline".replace("§7", filter.equals(ServerFilter.OFFLINE) ? "§a" : "§7")),
                Component.text("§7Alle".replace("§7", filter.equals(ServerFilter.ALL) ? "§a" : "§7")),
                Component.text("§7Deine".replace("§7", filter.equals(ServerFilter.YOURS) ? "§a" : "§7"))
        )), () -> {
            if(filter.equals(ServerFilter.ALL)) {
                new ServerListInventory(player, ServerFilter.YOURS);
            } else if(filter.equals(ServerFilter.YOURS)) {
                new ServerListInventory(player, ServerFilter.ONLINE);
            } else if(filter.equals(ServerFilter.ONLINE)) {
                new ServerListInventory(player, ServerFilter.OFFLINE);
            } else {
                new ServerListInventory(player, ServerFilter.ALL);
            }
        }));

        item(4, 8, new InteractItem(new ItemView(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).name("§cZurück"), () -> new HomeInventory(player)));

        open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 2f);
    }

    @Override
    public InteractItem constructItem(Server server) {
        var owner = PrivateServer.instance().userHandler().repository().query().find().stream().filter(it -> it.servers().contains(server)).findFirst().orElse(null);
        return new InteractItem(ItemView.of(server.icon()).name("§7" + server.name()).list(List.of(
                Component.text("§7Status §8» §7" + (server.runningId() == null ? "§cOFFLINE" : "§aONLINE")),
                Component.empty(),
                Component.text(server.description() == null ? "§fKeine Beschreibung gefunden." : "§f" + server.description()),
                Component.empty(),
                Component.text("§7Spieler §8» §70/0"),
                Component.text("§7Besitzer §8» §7" + NameFetcher.name(owner.uniqueId())),
                Component.empty(),
                Component.text("§7Letzter Start §8» §7" + (server.lastStarted() == -1 ? "Noch nie gestartet" : new Time(server.lastStarted()).toGMTString())),
                Component.text("§7Erstellt §8» §7" + new Time(server.created()).toGMTString())
        )), () -> {
            if(this.player().getUniqueId().equals(owner.uniqueId())) {
                new ServerInventory(this.player(), server);
            } else {
                this.player().sendMessage("CONNECT!");
            }
        });
    }*/
}

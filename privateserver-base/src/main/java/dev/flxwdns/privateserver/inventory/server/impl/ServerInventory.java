package dev.flxwdns.privateserver.inventory.server.impl;

import de.flxwdev.ascan.inventory.SingletonView;
import de.flxwdev.ascan.inventory.item.InteractItem;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.cloud.SimpleCloudHandler;
import dev.flxwdns.privateserver.inventory.server.ServerListInventory;
import dev.flxwdns.privateserver.inventory.server.filter.ServerFilter;
import dev.flxwdns.privateserver.inventory.subdomain.SubDomainListInventory;
import dev.flxwdns.privateserver.sign.SignBuilder;
import dev.flxwdns.privateserver.user.impl.Server;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public final class ServerInventory extends SingletonView {

    public ServerInventory(Player player, Server server) {
        super(player, Component.text("§7" + server.name()), 6, false);

        placeHolder(1);
        placeHolder(6);
        for (int i = 0; i < 5; i++) {
            placeHolder(1 + i, 0);
            placeHolder(1 + i, 1);
            placeHolder(1 + i, 2);
            placeHolder(1 + i, 8);
        }

        item(2, 1, new InteractItem(new ItemView(Material.RED_STAINED_GLASS).name("§cServer ist Offline").rawList(List.of(
                "§7",
                "§7Aktueller Status §8» §cOFFLINE",
                "§7",
                "§eKlick §8» §7Server starten§8."
        )), () -> {
            PrivateServer.instance().cloudHandler().start(player, server.serverUniqueId());
        }));

        item(4, 1, new InteractItem(new ItemView(Material.COMPARATOR).name("§eRAM").rawList(List.of(
                "§7",
                "§7Aktueller RAM §8» §e1024 MB",
                "§7",
                "§7Setze den RAM für deinen Server§8.",
                "§7Denk dran§8, §7jeder Server brauch RAM§8.",
                "§7",
                "§eKlick §8» §7RAM setzen§8."
        )), () -> player.sendMessage("§cNot implemented yet.")));

        item(5, 1, new InteractItem(new ItemView(Material.AMETHYST_SHARD).name("§eSlots").rawList(List.of(
                "§7",
                "§7Aktuelle Slots §8» §e5",
                "§7",
                "§7Setze die Slots für deinen Server§8.",
                "§7Die Slots sagen aus§8, §7wie viele Spieler",
                "§7gleichzeitig auf deinem Server spielen können§8.",
                "§7",
                "§eKlick §8» §7RAM setzen§8."
        )), () -> player.sendMessage("§cNot implemented yet.")));

        item(new InteractItem(new ItemView(Material.ITEM_FRAME).name("§eIcon setzen").rawList(List.of(
                "§7",
                "§7Setze das Icon für deinen Server§8.",
                "§7Das Icon wird in der Serverliste angezeigt§8.",
                "§7",
                "§eKlick §8» §7Icon setzen§8."
        )), () -> new ServerIconInventory(player, server, "")));

        item(new InteractItem(new ItemView(Material.ACACIA_SIGN).name("§eNamen ändern").rawList(List.of(
                "§7Aktueller Name §8» §e" + server.name(),
                "§7",
                "§7Setze den Namen für deinen Server§8.",
                "§7Denk dran§8, §7jeder Server brauch einen guten Namen§8.",
                "§7",
                "§eKlick §8» §7Name setzen§8."
        )), () -> {
            SignBuilder.buildSign(player, name -> {
                var user = PrivateServer.instance().userHandler().user(player);
                server.name(name);
                user.updateServer(server);
                PrivateServer.instance().userHandler().update(user);

                player.sendMessage("§aDer Name wurde erfolgreich geändert.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
                new ServerInventory(player, server);
            }, PrivateServer.instance());
        }));

        item(new InteractItem(new ItemView(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=")).name("§cServer löschen").rawList(List.of(
                "§7",
                "§cDieser Vorgang ist unwiderruflich§8.",
                "§cDenk dran§8, §cdu verlierst alle Daten§8!",
                "§7",
                "§eKlick §8» §7Server löschen§8."
        )), () -> player.sendMessage("§cNot implemented yet.")));

        item(new InteractItem(new ItemView(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).name("§cServer zurücksetzen").rawList(List.of(
                "§7",
                "§cDieser Vorgang ist unwiderruflich§8.",
                "§cDenk dran§8, §cdu verlierst alle Daten§8!",
                "§7",
                "§eKlick §8» §7Server zurücksetzen§8."
        )), () -> player.sendMessage("§cNot implemented yet.")));

        item(new InteractItem(new ItemView(Material.BOOK).name("§eBeschreibung").rawList(List.of(
                "§7Aktuelle Beschreibung §8» §e" + (server.description() == null ? "§7Keine" : server.description()),
                "§7",
                "§7Setze eine Beschreibung für deinen Server§8.",
                "§7Damit sammelst du schonmal die ersten Spieler§8.",
                "§7",
                "§eKlick §8» §7Beschreibung ändern§8."
        )), () -> {
            SignBuilder.buildSign(player, description -> {
                var user = PrivateServer.instance().userHandler().user(player);
                server.description(description);
                user.updateServer(server);
                PrivateServer.instance().userHandler().update(user);

                player.sendMessage("§aDie Beschreibung wurde erfolgreich geändert.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
                new ServerInventory(player, server);
            }, PrivateServer.instance());
        }));

        item(new InteractItem(new ItemView(Material.ANVIL).name("§eEinstellungen").rawList(List.of(
                "§7",
                "§7Stelle deinen Server ein§8.",
                "§7Hier kann alles mögliche Eingestellt werden§8.",
                "§7",
                "§eKlick §8» §7Einstellungen ändern§8."
        )), () -> player.sendMessage("§cNot implemented yet.")));

        item(new InteractItem(new ItemView(Material.COMMAND_BLOCK).name("§ePlugins").rawList(List.of(
                "§7",
                "§7Lade alle möglichen Plugin auf deinen Server§8.",
                "§7Solltest du WorldEdit brauchen§8? §7Kein Problem§8.",
                "§7",
                "§eKlick §8» §7Plugins einstellen§8."
        )), () -> player.sendMessage("§cNot implemented yet.")));

        item(new InteractItem(new ItemView(Material.LIGHT_BLUE_DYE).name("§eVerbinden").rawList(List.of(
                "§7",
                "§7Du hast es endlich geschafft alles einzustellen§8?",
                "§7Dann verbinde dich zu deinen Server§8!",
                "§7",
                "§eKlick §8» §7Zum Server verbinden§8."
        )), () -> player.sendMessage("§cNot implemented yet.")));

        item(6, 8, new InteractItem(ItemView.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ5ZDI3MWM1ZGY4NGY4YTNjOGFhNWQxNTQyN2Y2MjgzOTM0MWRhYjUyYzYxOWE1OTg3ZDM4ZmJlMThlNDY0In19fQ==")).name("§cZurück"), () -> {
            new ServerListInventory(player, ServerFilter.ALL);
        }));

        player.openInventory(this.inventory());
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
    }
}
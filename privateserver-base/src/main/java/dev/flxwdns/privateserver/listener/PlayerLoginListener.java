package dev.flxwdns.privateserver.listener;

import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.user.User;
import dev.flxwdns.privateserver.user.impl.Server;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public final class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        System.out.println("Connection over " + event.getHostname());

        var users = PrivateServer.instance().userHandler().repository()
                .query()
                .find();

        UUID serverUniqueId = null;
        for (User user : users) {
           var domain = user.domains().stream().filter(it -> it.domain().equalsIgnoreCase(event.getHostname())).findFirst().orElse(null);
           if(domain != null) {
               serverUniqueId = domain.connectedServer();
               break;
           }
        }

        if(serverUniqueId == null) {
            System.out.println("Domain not found or not connected.");
            return;
        }

        Server server = null;
        for (User user : users) {
            UUID finalServerUniqueId = serverUniqueId;
            var found = user.servers().stream().filter(it -> it.serverUniqueId().equals(finalServerUniqueId)).findFirst().orElse(null);
            if(found != null) {
                server = found;
                break;
            }
        }

        if(server != null) {
            var player = event.getPlayer();
            Server finalServer = server;
            Bukkit.getScheduler().runTaskLater(PrivateServer.instance(), () -> {
                player.sendTitle("§6Server gefunden", "§7Du wirst verbunden§8...", 20, 20 * 100, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                if(finalServer.runningId() == null) {
                    var id = PrivateServer.instance().cloudHandler().start(finalServer.serverUniqueId());
                    var user = PrivateServer.instance().userHandler().user(player);
                    finalServer.runningId(id);
                    user.updateServer(finalServer);
                    PrivateServer.instance().userHandler().update(user);

                    PrivateServer.instance().cloudHandler().queueConnect(player.getUniqueId(), id);
                } else {
                    PrivateServer.instance().cloudHandler().connect(player.getUniqueId(), finalServer.runningId());
                }
            }, 20 * 4);
        } else {
            System.out.println("Server not found");
        }
    }
}

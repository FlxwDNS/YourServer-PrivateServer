package dev.flxwdns.privateserver.listener;

import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.user.User;
import dev.flxwdns.privateserver.user.impl.Server;
import net.kyori.adventure.text.Component;
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
            if (domain != null) {
                serverUniqueId = domain.connectedServer();
                break;
            }
        }

        if (serverUniqueId == null) {
            System.out.println("Domain not found or not connected.");
            return;
        }

        Server server = null;
        for (User user : users) {
            UUID finalServerUniqueId = serverUniqueId;
            var found = user.servers().stream().filter(it -> it.serverUniqueId().equals(finalServerUniqueId)).findFirst().orElse(null);
            if (found != null) {
                server = found;
                break;
            }
        }

        if (server != null) {
            var player = event.getPlayer();
            Server finalServer = server;

            if (finalServer.runningId() == null) {
                var id = PrivateServer.instance().cloudHandler().start(finalServer.serverUniqueId());

                var user = PrivateServer.instance().userHandler().user(player.getUniqueId());
                finalServer.runningId(id);
                user.updateServer(finalServer);
                PrivateServer.instance().userHandler().update(user);
            } else {
                PrivateServer.instance().cloudHandler().connect(player.getUniqueId(), finalServer.runningId());
            }

            if(!PrivateServer.instance().cloudHandler().isJoinAble(finalServer.runningId())) {

                player.kick(Component.text(
                        "§8» §7Domain §8| §e" + event.getHostname().toLowerCase() + "\n\n§cYour server is starting§8.\n§cPlease try again in a few seconds§8.\n\n§7If you have any questions, please contact our support."
                ));
            }
        } else {
            System.out.println("Server not found");
        }
    }
}

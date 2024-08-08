package dev.flxwdns.privateserver.listener;

import dev.flxwdns.privateserver.PrivateServer;
import dev.flxwdns.privateserver.user.User;
import dev.flxwdns.privateserver.user.impl.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public final class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        System.out.println(event.getHostname());

        var users = PrivateServer.instance().userHandler().repository()
                .query()
                .find();

        UUID serverUniqueId = null;
        for (User user : users) {
           var domain = user.domains().stream().filter(it -> it.domain().equalsIgnoreCase(event.getHostname())).findFirst().orElse(null);
           if(domain != null) serverUniqueId = domain.connectedServer();
        }

        if(serverUniqueId == null) {
            System.out.println("Domain not found");
            return;
        }

        Server server = null;
        for (User user : users) {
            UUID finalServerUniqueId = serverUniqueId;
            server = user.servers().stream().filter(it -> it.serverUniqueId().equals(finalServerUniqueId)).findFirst().orElse(null);
        }

        if(server != null) {
            var player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 100, 10, false, false, false));
            player.sendTitle("§6Server gefunden", "§7Du wirst verbunden§8...", 20, 20 * 100, 20);

            if(server.runningId() == null) {
                PrivateServer.instance().cloudHandler().start(server.serverUniqueId());
            }
            PrivateServer.instance().cloudHandler().connect(player.getUniqueId(), server.runningId());
        } else {
            System.out.println("Server not found");
        }
    }
}

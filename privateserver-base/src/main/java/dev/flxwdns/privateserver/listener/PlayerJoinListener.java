package dev.flxwdns.privateserver.listener;

import dev.flxwdns.privateserver.PrivateServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        PrivateServer.instance().userHandler().create(player);
    }
}

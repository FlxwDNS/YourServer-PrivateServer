package dev.flxwdns.privateserver.cloud;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface CloudHandler {
    void start(Player player, UUID serverId);
}

package dev.flxwdns.privateserver.command;

import dev.flxwdns.privateserver.inventory.HomeInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PrivateServerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String arg, @NotNull String[] args) {
        if(sender instanceof Player player) {
            if(player.hasPermission("privateserver.command")) {
                new HomeInventory(player);
                return true;
            }
            return false;
        }
        sender.sendMessage("§cYou must be a player to execute this command.");
        return false;
    }
}

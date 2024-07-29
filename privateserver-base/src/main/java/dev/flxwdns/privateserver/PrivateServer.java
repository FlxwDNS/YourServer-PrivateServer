package dev.flxwdns.privateserver;

import de.flxwdev.ascan.AscanLayer;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import de.flxwdev.ascan.misc.Config;
import dev.flxwdns.privateserver.command.PrivateServerCommand;
import dev.flxwdns.privateserver.listener.PlayerJoinListener;
import dev.flxwdns.privateserver.user.UserHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class PrivateServer extends JavaPlugin {
    @Getter
    private static PrivateServer instance;

    private UserHandler userHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        this.userHandler = new UserHandler();

        AscanLayer.init(this, new Config()
                .placeHolder(Material.GRAY_STAINED_GLASS_PANE)
                .arrowLeft(ItemView.of(
                        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg5ZGQ3YWY0YzgwM2I1Mjg3YzQzMzcwN2M3YzQzN2NjMjhkNTIxYmI2ODJjNDdhNGQzZDVkMmE0OGFmYTYifX19")
                ).name("§7Zurück"))
                .arrowRight(ItemView.of(
                        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWEyNmU1ZmYxODY3NzhlZWU2ZGJmOThhMTUwNzQzODRjMzIxMWQxNmJlMGYyOTQ2MGJiZDk2NGFlZmYifX19")
                ).name("§7Vor"))
        );

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        this.getCommand("privateserver").setExecutor(new PrivateServerCommand());
    }
}

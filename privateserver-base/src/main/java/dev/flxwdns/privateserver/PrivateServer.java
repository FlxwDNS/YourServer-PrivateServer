package dev.flxwdns.privateserver;

import de.flxwdev.ascan.AscanLayer;
import de.flxwdev.ascan.inventory.item.ItemView;
import de.flxwdev.ascan.inventory.item.SkullCreator;
import de.flxwdev.ascan.misc.Config;
import dev.flxwdns.privateserver.cloud.CloudHandler;
import dev.flxwdns.privateserver.cloud.SimpleCloudHandler;
import dev.flxwdns.privateserver.command.PrivateServerCommand;
import dev.flxwdns.privateserver.listener.PlayerJoinListener;
import dev.flxwdns.privateserver.listener.PlayerLoginListener;
import dev.flxwdns.privateserver.user.User;
import dev.flxwdns.privateserver.user.UserHandler;
import dev.flxwdns.privateserver.user.impl.Server;
import dev.httpmarco.evelon.layer.connection.ConnectionAuthenticationPath;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class PrivateServer extends JavaPlugin {
    @Getter
    private static PrivateServer instance;

    private UserHandler userHandler;
    private CloudHandler cloudHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        ConnectionAuthenticationPath.set(Path.of("plugins/YourServer/auth.json").toAbsolutePath().toString());

        this.userHandler = new UserHandler();

        switch (this.getConfig().getString("cloud-handler")) {
            case "simplecloud":
                this.cloudHandler = new SimpleCloudHandler();
                break;
            case null:
                throw new RuntimeException("Cloud handler is null.");
            default:
                throw new RuntimeException("Unknown cloud handler.");
        }
        this.cloudHandler.onServiceShutdown(serviceId -> {
            Map<Server, User> servers = new HashMap<>();

            for (User user : this.userHandler.repository().query().find()) {
                user.servers().stream().filter(it -> it.runningId() != null && it.runningId().equalsIgnoreCase(serviceId)).forEach(it -> servers.put(it, user));
            }
            servers.forEach((server, user) -> {
                server.runningId(null);
                user.updateServer(server);

                this.userHandler.update(user);
            });
        });

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
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);

        this.getCommand("privateserver").setExecutor(new PrivateServerCommand());

        Bukkit.getScheduler().runTaskTimer(PrivateServer.instance(), () -> {
            this.cloudHandler.queueConnect().forEach((uuid, s) -> {
                if (Bukkit.getOnlinePlayers().stream().noneMatch(it -> it.getUniqueId().equals(uuid))) this.cloudHandler.queueConnect().remove(uuid);

                var player = Bukkit.getPlayer(uuid);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 4, 1, false, false, false));
                for (int x = 0; x < 100; x++) {
                    for (int z = 0; z < 100; z++) {
                        player.sendBlockChange(player.getLocation().clone().add(x - 50, 0, z - 50), Material.BARRIER.createBlockData());
                    }
                }
                player.sendActionBar(Component.text(this.getConfig().getString("prefix") + " §7Verbinde§8..."));
            });
        }, 0, 20);
    }
}

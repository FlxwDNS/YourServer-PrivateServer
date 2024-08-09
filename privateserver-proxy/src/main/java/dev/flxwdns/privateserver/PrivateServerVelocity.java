package dev.flxwdns.privateserver;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.flxwdns.privateserver.cloud.CloudHandler;
import dev.flxwdns.privateserver.cloud.SimpleCloudHandler;
import dev.flxwdns.privateserver.user.User;
import dev.flxwdns.privateserver.user.UserHandler;
import dev.flxwdns.privateserver.user.impl.Server;
import dev.httpmarco.evelon.layer.connection.ConnectionAuthenticationPath;
import net.kyori.adventure.text.Component;

import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;

@Plugin(id = "yourserver", name = "YourServer", version = "1.0", description = "YourServer is a private server plugin for Velocity.", authors = {"flxwdns"})
public final class PrivateServerVelocity {
    private final ProxyServer server;
    private final Logger logger;

    private final UserHandler userHandler;
    private final CloudHandler cloudHandler;

    @Inject
    public PrivateServerVelocity(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        ConnectionAuthenticationPath.set(Path.of("plugins/YourServer/auth.json").toAbsolutePath().toString());

        this.userHandler = new UserHandler();
        this.cloudHandler = new SimpleCloudHandler();
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onProxyPing(ProxyPingEvent event) {
        var hostName = event.getConnection().getVirtualHost().orElseThrow().getHostName();
        var server = serverOrNull(hostName);

        if(server != null) {
            event.setPing(ServerPing.builder()
                    .description(Component.text("§8| §f§l" + server.name() + "\n§8» §7Domain §8| §e" + hostName.toLowerCase() + " §8- §7" + (server.description() == null ? "§cKeine Beschreibung" : server.description())))
                    .build());
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        var hostName = event.getPlayer().getVirtualHost().orElseThrow().getHostName();
        var server = serverOrNull(hostName);

        if(server != null && this.cloudHandler.isJoinAble(server.runningId())) {
            var initServer = this.server.getServer(server.runningId()).orElse(null);
            if(initServer == null) {
                System.out.println("Server is not ready. " + server.runningId());
                return;
            }

            event.setInitialServer(initServer);
        }
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        var hostName = event.getConnection().getVirtualHost().orElseThrow().getHostName();
        System.out.println("Connection over " + hostName);

        Server server = serverOrNull(hostName);
        if (server != null) {
            Server finalServer = server;

            if (finalServer.runningId() == null) {
                var id = this.cloudHandler.start(finalServer.serverUniqueId());
                System.out.println("Service will be started: " + id);

                var user = this.userHandler.user(event.getUniqueId());
                finalServer.runningId(id);
                user.updateServer(finalServer);
                this.userHandler.update(user);
            } else {
                event.setResult(PreLoginEvent.PreLoginComponentResult.allowed());
            }

            if(!this.cloudHandler.isJoinAble(finalServer.runningId())) {
                event.setResult(PreLoginEvent.PreLoginComponentResult.denied((Component.text(
                        "§8» §7Domain §8| §e" + hostName.toLowerCase() + "\n\n§cYour server is starting§8.\n§cPlease try again in a few seconds§8.\n\n§7If you have any questions, please contact our support."
                ))));
            }
        } else {
            System.out.println("Server not found");
        }
    }

    private Server serverOrNull(String hostName) {
        var users = this.userHandler.repository()
                .query()
                .find();

        UUID serverUniqueId = null;
        for (User user : users) {
            var domain = user.domains().stream().filter(it -> it.domain().equalsIgnoreCase(hostName)).findFirst().orElse(null);
            if (domain != null) {
                serverUniqueId = domain.connectedServer();
                break;
            }
        }

        if (serverUniqueId == null) {
            System.out.println("Domain not found or not connected.");
            return null;
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
        return server;
    }
}

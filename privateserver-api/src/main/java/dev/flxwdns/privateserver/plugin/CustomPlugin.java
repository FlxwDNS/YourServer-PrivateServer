package dev.flxwdns.privateserver.plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class CustomPlugin {
    private final String name;
    private final Material material;
    private final String description;
    private final String id;

    private Path path(UUID serverUniqueId) {
        return Path.of("../../").resolve("yourserver-saves").resolve(serverUniqueId.toString()).resolve("plugins").resolve(name + "(#" + id + ").jar");
    }

    public boolean isInstalled(UUID serverUniqueId) {
        return path(serverUniqueId).toFile().exists();
    }

    @SneakyThrows
    public void download(UUID serverUniqueId) {
        var path = Path.of("../../").resolve("yourserver-saves").resolve(serverUniqueId.toString()).resolve("plugins");
        path.toFile().mkdirs();

        if(!id.startsWith("https://") && !id.contains(".")) {
            var in = new URL("https://api.spiget.org/v2/resources/" + this.id + "/download").openStream();
            Files.copy(in, path(serverUniqueId), StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        if(id.endsWith(".jar")) {
            Files.copy(new URL(this.id).openStream(), path(serverUniqueId), StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        throw new RuntimeException("Invalid ID: " + id + " (must be a SpigotMC resource ID or a direct JAR download URL)");
    }
}

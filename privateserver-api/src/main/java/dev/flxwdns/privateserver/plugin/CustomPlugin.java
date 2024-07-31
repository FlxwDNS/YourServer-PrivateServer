package dev.flxwdns.privateserver.plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public final class CustomPlugin {
    private final String name;
    private final Material material;
    private final String description;
    private final String id;

    public void download(Path path) {
        if(!id.startsWith("https://") && !id.contains(".")) {
            return;
        }
        if(id.endsWith(".jar")) {
            return;
        }
        throw new RuntimeException("Invalid ID: " + id + " (must be a SpigotMC resource ID or a direct JAR download URL)");
    }
}

package dev.flxwdns.privateserver.plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public final class CustomPlugin {
    private final String url;

    public String name() {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public void download(Path path) {
        if(url.startsWith("https://www.spigotmc.org/resources/")) {

            return;
        }
        if(url.endsWith(".jar")) {
            return;
        }
        throw new RuntimeException("Invalid plugin URL: " + url + " (must be a SpigotMC resource URL or a direct JAR download URL)");
    }
}

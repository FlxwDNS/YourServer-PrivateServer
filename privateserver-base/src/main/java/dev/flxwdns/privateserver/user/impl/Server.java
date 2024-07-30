package dev.flxwdns.privateserver.user.impl;

import dev.httpmarco.evelon.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.util.UUID;

@Data
@AllArgsConstructor
public final class Server {
    @PrimaryKey
    private final UUID serverUniqueId;

    private long lastStarted;
    private long created;

    private String runningId;

    private String name;
    private String description;
    private Material icon;
}

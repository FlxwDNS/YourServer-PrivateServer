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

    private long created;

    private final String name;
    private final String description;
    private Material icon;
}

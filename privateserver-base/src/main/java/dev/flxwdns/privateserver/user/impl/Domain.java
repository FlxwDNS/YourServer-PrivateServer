package dev.flxwdns.privateserver.user.impl;

import dev.httpmarco.evelon.PrimaryKey;
import lombok.Data;

import java.util.UUID;

@Data
public final class Domain {
    @PrimaryKey
    private final String domain;

    private UUID connectedServer;
}

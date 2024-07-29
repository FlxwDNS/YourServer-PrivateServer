package dev.flxwdns.privateserver.user;

import dev.flxwdns.privateserver.user.impl.Domain;
import dev.flxwdns.privateserver.user.impl.Server;
import dev.httpmarco.evelon.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class User {
    @PrimaryKey
    private final UUID uniqueId;

    private final List<Server> servers;
    private final List<Domain> domains;
}

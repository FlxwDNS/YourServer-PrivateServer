package dev.flxwdns.privateserver.user;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class UserHandler {
    @Getter
    private final Repository<User> repository;
    private final Map<UUID, User> cache;

    public UserHandler() {
        this.repository = Repository.build(User.class).withLayer(MariaDbLayer.class).withId("users").build();
        this.cache = new HashMap<>();
    }

    public User user(UUID uniqueId) {
        if (this.cache.containsKey(uniqueId)) {
            return this.cache.get(uniqueId);
        }
        var user = this.repository.query().match("uniqueId", uniqueId).findFirst();
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        this.cache.put(uniqueId, user);

        return user;
    }

    public boolean exists(UUID uniqueId) {
        if (this.cache.containsKey(uniqueId)) return true;

        return this.repository.query().match("uniqueId", uniqueId).exists();
    }

    public void create(UUID uniqueId) {
        if (this.exists(uniqueId)) return;

        this.repository.query().create(new User(uniqueId, new ArrayList<>(), new ArrayList<>()));
    }

    public void update(User user) {
        this.cache.put(user.uniqueId(), user);
        this.repository.query().match("uniqueId", user.uniqueId()).update(user);
    }
}

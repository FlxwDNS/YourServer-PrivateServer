package dev.flxwdns.privateserver.user;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;

import java.util.ArrayList;
import java.util.UUID;

public final class UserHandler {
    @Getter
    private final Repository<User> repository;

    public UserHandler() {
        this.repository = Repository.build(User.class).withLayer(MariaDbLayer.class).withId("users").build();
    }

    public User user(UUID uniqueId) {
        var user = this.repository.query().match("uniqueId", uniqueId).findFirst();
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        return user;
    }

    public boolean exists(UUID uniqueId) {
        return this.repository.query().match("uniqueId", uniqueId).exists();
    }

    public void create(UUID uniqueId) {
        if (this.exists(uniqueId)) return;

        this.repository.query().create(new User(uniqueId, new ArrayList<>(), new ArrayList<>()));
    }

    public void update(User user) {
        this.repository.query().match("uniqueId", user.uniqueId()).update(user);
    }
}

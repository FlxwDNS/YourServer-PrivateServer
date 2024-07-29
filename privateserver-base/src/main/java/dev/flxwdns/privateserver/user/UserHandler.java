package dev.flxwdns.privateserver.user;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;
import org.bukkit.entity.Player;

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

    public User user(Player player) {
        if (this.cache.containsKey(player.getUniqueId())) {
            return this.cache.get(player.getUniqueId());
        }
        var user = this.repository.query().match("uniqueId", player.getUniqueId()).findFirst();
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        this.cache.put(player.getUniqueId(), user);

        return user;
    }

    public boolean exists(Player player) {
        if (this.cache.containsKey(player.getUniqueId())) return true;

        return this.repository.query().match("uniqueId", player.getUniqueId()).exists();
    }

    public void create(Player player) {
        if (this.exists(player)) return;

        this.repository.query().create(new User(player.getUniqueId(), new ArrayList<>(), new ArrayList<>()));
    }

    public void update(User user) {
        this.cache.put(user.uniqueId(), user);
        this.repository.query().match("uniqueId", user.uniqueId()).update(user);
    }
}

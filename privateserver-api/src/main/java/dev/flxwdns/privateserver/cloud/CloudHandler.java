package dev.flxwdns.privateserver.cloud;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface CloudHandler {
    void onServiceShutdown(Consumer<String> id);

    String start(UUID serverId);
    void shutdown(String serverId);

    void connect(UUID uniqueId, String serverId);
    void queueConnect(UUID uniqueId, String serverId);

    boolean isJoinAble(String serverId);

    Map<UUID, String> queueConnect();
}

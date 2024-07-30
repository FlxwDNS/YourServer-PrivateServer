package dev.flxwdns.privateserver.cloud;

import java.util.UUID;
import java.util.function.Consumer;

public interface CloudHandler {
    void onServiceShutdown(Consumer<String> id);

    String start(UUID serverId);
    void shutdown(String serverId);

    void connect(UUID uniqueId, String serverId);

    boolean isRunning(UUID serverId);
}

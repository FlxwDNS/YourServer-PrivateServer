package dev.flxwdns.privateserver.cloud;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.event.service.CloudServiceConnectedEvent;
import eu.thesimplecloud.api.event.service.CloudServiceUnregisteredEvent;
import eu.thesimplecloud.api.event.service.CloudServiceUpdatedEvent;
import eu.thesimplecloud.api.eventapi.CloudEventHandler;
import eu.thesimplecloud.api.eventapi.IListener;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SimpleCloudHandler implements CloudHandler, IListener {
    private final Map<UUID, String> queueConnect;

    private Consumer<String> shutdownConsumer;

    public SimpleCloudHandler() {
        this.queueConnect = new ConcurrentHashMap<>();

        CloudAPI.getInstance().getEventManager().registerListener(CloudAPI.getInstance().getThisSidesCloudModule(), this);
    }

    @CloudEventHandler
    public void handle(CloudServiceUpdatedEvent event) {
        if(!event.getCloudService().isServiceJoinable()) return;

        this.queueConnect.entrySet().stream().filter(it -> it.getValue().equals(event.getCloudService().getDisplayName())).forEach(it -> {
            if(!it.getValue().equals(event.getCloudService().getDisplayName())) return;

            this.connect(it.getKey(), it.getValue());
            this.queueConnect.remove(it.getKey());
        });
    }

    @CloudEventHandler
    public void handle(CloudServiceUnregisteredEvent event) {
        System.out.println("Service " + event.getCloudService().getDisplayName() + " has been unregistered");
        if(shutdownConsumer != null) {
            System.out.println("Consumer has been called");
            shutdownConsumer.accept(event.getCloudService().getDisplayName());
        }
    }

    @Override
    public void onServiceShutdown(Consumer<String> id) {
        this.shutdownConsumer = id;
    }

    @Override
    @SneakyThrows
    public String start(UUID serverId) {
        var template = CloudAPI.getInstance().getTemplateManager().getTemplateByName("PS");
        if(template == null) throw new RuntimeException("Template PS not found");
        var serviceGroup = CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName("PS");
        var savePath = Path.of("../../").resolve("yourserver-saves");
        savePath.toFile().mkdirs();

        var startConfiguration = serviceGroup.createStartConfiguration()
                .setMaxMemory(512)
                .setMaxPlayers(5)
                .setTemplate(template);
        var service = startConfiguration.startService().get();

        if(savePath.resolve(serverId.toString()).toFile().exists()) {
            FileUtils.copyDirectory(savePath.resolve(serverId.toString()).toFile(), savePath.getParent().resolve("tmp").resolve(service.getDisplayName()).toFile());
        } else {
            savePath.resolve(serverId.toString()).toFile().mkdirs();
        }
        return service.getDisplayName();
    }

    @Override
    public void shutdown(String serverId) {
        var service = CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(serverId);
        if(service != null) {
            service.shutdown();
        }
    }

    @Override
    @SneakyThrows
    public void connect(UUID uniqueId, String serverId) {
        var service = CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(serverId);
        if(service != null) {
            CloudAPI.getInstance().getCloudPlayerManager().getCloudPlayer(uniqueId).get().connect(service);
        }
    }

    @Override
    public void queueConnect(UUID uniqueId, String serverId) {

        System.out.println(uniqueId + " " + serverId);

        this.queueConnect.put(uniqueId, serverId);
    }

    @Override
    public boolean isRunning(UUID serverId) {
        var service = CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(serverId.toString());
        return service != null && service.isOnline();
    }

    @Override
    public Map<UUID, String> queueConnect() {
        return this.queueConnect;
    }
}

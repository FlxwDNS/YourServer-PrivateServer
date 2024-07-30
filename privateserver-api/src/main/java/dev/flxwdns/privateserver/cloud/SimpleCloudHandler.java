package dev.flxwdns.privateserver.cloud;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.event.service.CloudServiceUnregisteredEvent;
import eu.thesimplecloud.api.eventapi.CloudEventHandler;
import eu.thesimplecloud.api.eventapi.IListener;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Consumer;

public class SimpleCloudHandler implements CloudHandler, IListener {
    private Consumer<String> shutdownConsumer;

    public SimpleCloudHandler() {
        CloudAPI.getInstance().getEventManager().registerListener(CloudAPI.getInstance().getThisSidesCloudModule(), this);
    }

    @CloudEventHandler
    public void handle(CloudServiceUnregisteredEvent event) {
        System.out.println("Service " + event.getCloudService().getDisplayName() + " has been unregistered");
        if(shutdownConsumer != null) {
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
                .setTemplate(template)
                .setServiceNumber(1);
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
    public boolean isRunning(UUID serverId) {
        var service = CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(serverId.toString());
        return service != null && service.isOnline();
    }
}

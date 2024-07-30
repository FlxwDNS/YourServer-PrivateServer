package dev.flxwdns.privateserver.cloud;

import eu.thesimplecloud.api.CloudAPI;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.UUID;

public class SimpleCloudHandler implements CloudHandler {

    @Override
    @SneakyThrows
    public void start(Player player, UUID serverId) {
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
    }
}

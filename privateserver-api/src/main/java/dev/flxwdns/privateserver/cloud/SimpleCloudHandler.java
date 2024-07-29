package dev.flxwdns.privateserver.cloud;

import eu.thesimplecloud.api.CloudAPI;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SimpleCloudHandler implements CloudHandler {

    @Override
    @SneakyThrows
    public void start(Player player, UUID serverId) {
        var template = CloudAPI.getInstance().getTemplateManager().getTemplateByName("PS");
        if(template == null) throw new RuntimeException("Template PS not found");
        var serviceGroup = CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName("PS");

        var service = serviceGroup.createStartConfiguration()
                .setMaxMemory(512)
                .setMaxPlayers(5)
                .setTemplate(template)
                .setServiceNumber(1)
                .startService()
                .get();
        service.setDisplayName("PS-Server-" + serverId);
    }
}

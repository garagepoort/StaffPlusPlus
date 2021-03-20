package net.shortninja.staffplus.domain.staff.broadcast;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.bungee.BungeeAction;
import net.shortninja.staffplus.common.bungee.BungeeClient;
import net.shortninja.staffplus.common.bungee.BungeeContext;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.broadcast.config.BroadcastConfiguration;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BroadcastService {

    private final MessageCoordinator message;
    private final BroadcastConfiguration broadcastConfiguration;
    private final BungeeClient bungeeClient;

    public BroadcastService(MessageCoordinator message, Options options, BungeeClient bungeeClient) {
        this.message = message;
        this.broadcastConfiguration = options.broadcastConfiguration;
        this.bungeeClient = bungeeClient;
    }

    void handleBungeeBroadcast(String message) {
        broadcast(message);
    }

    public void broadcastToCurrent(String message) {
        if (broadcastConfiguration.multipleServers()) {
            throw new BusinessException("&CNot allowed to use server [current] when configuration contains a list of network servers. Please specify the server name instead of [current].");
        }
        broadcast(message);
    }

    public void broadcastToAll(CommandSender sender, String message) {
        List<String> networkServers = broadcastConfiguration.getEnabledServers();
        if (broadcastConfiguration.sendToCurrent()) {
            broadcast(message);
        } else if (broadcastConfiguration.sendToAll()) {
            broadcast(message);
            bungeeClient.sendAll(sender, BungeeAction.FORWARD, BungeeContext.BROADCAST, message);
        } else {
            for (String networkServer : networkServers) {
                bungeeClient.send(sender, BungeeAction.FORWARD, networkServer, BungeeContext.BROADCAST, message);
            }
        }
    }

    public void broadcastToSpecific(CommandSender sender, List<String> servers, String message) {
        if(broadcastConfiguration.sendToCurrent()) {
            throw new BusinessException("&CConfiguration is set up to use current server. Cannot broadcast to another server");
        }
        List<String> invalidServers = servers.stream()
            .filter(s -> !broadcastConfiguration.getEnabledServers().contains(s))
            .collect(Collectors.toList());

        if (!invalidServers.isEmpty()) {
            throw new BusinessException("&CCannot use server names: [" + String.join(" - ", invalidServers) + "]");
        }

        for (String server : servers) {
            bungeeClient.send(sender, BungeeAction.FORWARD, server, BungeeContext.BROADCAST, message);
        }
    }

    private void broadcast(String message) {
        String[] lines = message.split(Pattern.quote("\\n"));
        for (String line : lines) {
            this.message.sendGlobalMessage(line, IocContainer.getOptions().broadcastConfiguration.getPrefix());
        }
    }
}

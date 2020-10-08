package net.shortninja.staffplus.staff.broadcast;

import com.google.common.io.ByteArrayDataOutput;
import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.broadcast.config.BroadcastConfiguration;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.io.ByteStreams.newDataOutput;
import static net.shortninja.staffplus.common.Constants.BUNGEE_CORD_CHANNEL;

public class BroadcastService {

    private final MessageCoordinator message;
    private final BroadcastConfiguration broadcastConfiguration;

    public BroadcastService(MessageCoordinator message, Options options) {
        this.message = message;
        this.broadcastConfiguration = options.broadcastConfiguration;
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
            sendBungeeMessage(sender, "ALL", message);
        } else {
            for (String networkServer : networkServers) {
                sendBungeeMessage(sender, networkServer, message);
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
            sendBungeeMessage(sender, server, message);
        }
    }

    private void broadcast(String message) {
        String[] lines = message.split(Pattern.quote("\\n"));
        for (String line : lines) {
            this.message.sendGlobalMessage(line, IocContainer.getOptions().broadcastConfiguration.getPrefix());
        }
    }

    private void sendBungeeMessage(CommandSender sender, String server, String message) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            if (onlinePlayers.iterator().hasNext()) {
                player = onlinePlayers.iterator().next();
            }
        }
        if (player != null) {
            try {
                ByteArrayDataOutput out = newDataOutput();
                out.writeUTF("Forward");
                out.writeUTF(server);
                out.writeUTF("StaffPlusPlusBroadcast");
                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                msgout.writeUTF(message);

                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());

                player.sendPluginMessage(StaffPlus.get(), BUNGEE_CORD_CHANNEL, out.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package net.shortninja.staffplus.staffchat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.clip.placeholderapi.PlaceholderAPI;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import static net.shortninja.staffplus.util.MessageCoordinator.colorize;

public class StaffChatService {

    private static final String BUNGEE_CORD = "BungeeCord";
    private Messages messages;
    private Options options;

    public StaffChatService(Messages messages, Options options) {
        this.messages = messages;
        this.options = options;
    }

    void handleBungeeMessage(String message) {
        sendMessageToStaffMembers(message);
    }

    public void sendMessage(CommandSender sender, String message) {
        String name = sender instanceof Player ? sender.getName() : "Console";
        message = messages.staffChat.replace("%player%", name).replace("%message%", message);
        if (!messages.prefixStaffChat.isEmpty()) {
            message = messages.prefixStaffChat + " " + message;
        }

        sendBungeeMessage(sender, message);
        sendMessageToStaffMembers(message);
    }

    private void sendMessageToStaffMembers(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (StaffPlus.get().usesPlaceholderAPI) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }

            if (player.hasPermission(options.permissionStaffChat)) {
                player.sendMessage(colorize(message));
            }
        }
    }

    private void sendBungeeMessage(CommandSender sender, String message) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            if(onlinePlayers.iterator().hasNext()) {
                player = onlinePlayers.iterator().next();
            }
        }
        if(player != null) {
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF("StaffPlusPlusChat");
                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                msgout.writeUTF(message);

                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());

                player.sendPluginMessage(StaffPlus.get(), BUNGEE_CORD, out.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

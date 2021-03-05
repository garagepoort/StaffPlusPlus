package net.shortninja.staffplus.staff.staffchat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.clip.placeholderapi.PlaceholderAPI;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.Constants;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import static net.shortninja.staffplus.util.BukkitUtils.sendEvent;
import static net.shortninja.staffplus.util.MessageCoordinator.colorize;

public class StaffChatService implements net.shortninja.staffplusplus.staffmode.chat.StaffChatService {

    private Messages messages;
    private Options options;

    public StaffChatService(Messages messages, Options options) {
        this.messages = messages;
        this.options = options;
    }

    void handleBungeeMessage(String message) {
        sendMessageToStaff(message);
    }

    public void sendMessage(Player sender, String message) {
        String formattedMessage = messages.staffChat.replace("%player%", sender.getName()).replace("%message%", message);
        if (!messages.prefixStaffChat.isEmpty()) {
            formattedMessage = messages.prefixStaffChat + " " + formattedMessage;
        }

        sendBungeeMessage(sender, formattedMessage);
        sendMessageToStaff(formattedMessage);
        sendEvent(new StaffChatEvent(sender, options.serverName, message));
    }

    public boolean hasHandle(String message) {
        return message.startsWith(options.staffChatConfiguration.getHandle()) && !options.staffChatConfiguration.getHandle().isEmpty();
    }

    @Override
    public void sendMessage(String senderName, String message) {
        message = messages.staffChat
            .replace("%player%", senderName)
            .replace("%message%", message);
        if (!messages.prefixStaffChat.isEmpty()) {
            message = messages.prefixStaffChat + " " + message;
        }

        sendMessageToStaff(message);
    }

    private void sendMessageToStaff(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (StaffPlus.get().usesPlaceholderAPI) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }

            if (player.hasPermission(options.staffChatConfiguration.getPermissionStaffChat())) {
                player.sendMessage(colorize(message));
            }
        }
    }

    private void sendBungeeMessage(CommandSender sender, String message) {
        if (!options.staffChatConfiguration.isBungeeEnabled()) {
            // Bungee network not enabled
            return;
        }

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
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF("StaffPlusPlusChat");
                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                msgout.writeUTF(message);

                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());

                player.sendPluginMessage(StaffPlus.get(), Constants.BUNGEE_CORD_CHANNEL, out.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

package net.shortninja.staffplus.domain.staff.staffchat;

import me.clip.placeholderapi.PlaceholderAPI;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.config.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.shortninja.staffplus.common.utils.MessageCoordinator.colorize;

public class StaffChatMessageFormatter {

    private final Messages messages;

    public StaffChatMessageFormatter(Messages messages) {
        this.messages = messages;
    }

    String formatMessage(CommandSender sender, String chatMessage) {
        String formattedMessage = messages.staffChat.replace("%player%", sender.getName()).replace("%message%", chatMessage);
        if (!messages.prefixStaffChat.isEmpty()) {
            formattedMessage = messages.prefixStaffChat + " " + formattedMessage;
        }
        if (StaffPlus.get().usesPlaceholderAPI && sender instanceof Player) {
            formattedMessage = PlaceholderAPI.setPlaceholders((Player) sender, formattedMessage);
        }
        return colorize(formattedMessage);
    }

    String formatMessage(String senderName, String chatMessage) {
        String formattedMessage = messages.staffChat.replace("%player%", senderName).replace("%message%", chatMessage);
        if (!messages.prefixStaffChat.isEmpty()) {
            formattedMessage = messages.prefixStaffChat + " " + formattedMessage;
        }
        return colorize(formattedMessage);
    }
}

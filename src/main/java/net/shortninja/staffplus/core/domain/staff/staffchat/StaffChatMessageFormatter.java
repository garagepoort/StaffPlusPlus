package net.shortninja.staffplus.core.domain.staff.staffchat;

import be.garagepoort.mcioc.IocBean;
import me.clip.placeholderapi.PlaceholderAPI;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@IocBean
public class StaffChatMessageFormatter {

    private final Messages messages;
    private final MessageCoordinator message;

    public StaffChatMessageFormatter(Messages messages, MessageCoordinator message) {
        this.messages = messages;
        this.message = message;
    }

    String formatMessage(CommandSender sender, String chatMessage) {
        String formattedMessage = messages.staffChat.replace("%player%", sender.getName()).replace("%message%", chatMessage);
        if (StaffPlus.get().usesPlaceholderAPI && sender instanceof Player) {
            formattedMessage = PlaceholderAPI.setPlaceholders((Player) sender, formattedMessage);
        }
        return message.colorize(formattedMessage);
    }

    String formatMessage(String senderName, String chatMessage) {
        String formattedMessage = messages.staffChat.replace("%player%", senderName).replace("%message%", chatMessage);
        return message.colorize(formattedMessage);
    }
}

package net.shortninja.staffplus.core.domain.staff.staffchat;

import be.garagepoort.mcioc.IocBean;
import me.clip.placeholderapi.PlaceholderAPI;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@IocBean
public class StaffChatMessageFormatter {

    private final Messages messages;


    public StaffChatMessageFormatter(Messages messages) {
        this.messages = messages;

    }

    String formatMessage(CommandSender sender, String chatMessage) {
        String formattedMessage = messages.staffChat.replace("%player%", sender.getName()).replace("%message%", chatMessage);
        if (StaffPlus.get().usesPlaceholderAPI && sender instanceof Player) {
            formattedMessage = PlaceholderAPI.setPlaceholders((Player) sender, formattedMessage);
        }
        return messages.colorize(formattedMessage);
    }

    String formatMessage(String senderName, String chatMessage) {
        String formattedMessage = messages.staffChat.replace("%player%", senderName).replace("%message%", chatMessage);
        return messages.colorize(formattedMessage);
    }
}

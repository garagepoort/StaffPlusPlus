package net.shortninja.staffplus.core.domain.staff.staffchat;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.PlaceholderService;
import net.shortninja.staffplus.core.application.config.Messages;
import org.bukkit.command.CommandSender;

@IocBean
public class StaffChatMessageFormatter {

    private final Messages messages;
    private final PlaceholderService placeholderService;

    public StaffChatMessageFormatter(Messages messages, PlaceholderService placeholderService) {
        this.messages = messages;
        this.placeholderService = placeholderService;
    }

    String formatMessage(CommandSender sender, StaffChatChannelConfiguration channel, String chatMessage) {
        String formattedMessage = channel.getMessageFormat().replace("%player%", sender.getName()).replace("%message%", chatMessage);
        formattedMessage = placeholderService.setPlaceholders(sender, formattedMessage);
        return messages.colorize(formattedMessage);
    }

    String formatMessage(String senderName, StaffChatChannelConfiguration channel, String chatMessage) {
        String formattedMessage = channel.getMessageFormat().replace("%player%", senderName).replace("%message%", chatMessage);
        return messages.colorize(formattedMessage);
    }
}

package net.shortninja.staffplus.core.application.config.messages;

import org.bukkit.ChatColor;

public abstract class AbstractMessageSender implements MessageSender{


    public String colorize(String message) {
        message = message.replaceAll("&&", "<ampersand>");
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message.replaceAll("<ampersand>", "&");
    }

}

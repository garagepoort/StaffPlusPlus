package net.shortninja.staffplus.core.application.config.messages;

import org.bukkit.command.CommandSender;

public interface MessageSender {

    void sendMessage(CommandSender receiver, String message, String prefix);
}

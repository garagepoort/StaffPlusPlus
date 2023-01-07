package net.shortninja.staffplus.core.application.config.messages;

import be.garagepoort.mcioc.IocBean;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@IocBean
public class MessageSenderFactory {

    private final BukkitMessageSender bukkitMessageSender;
    private final JsonMessageSender jsonMessageSender;

    public MessageSenderFactory(BukkitMessageSender bukkitMessageSender, JsonMessageSender jsonMessageSender) {
        this.bukkitMessageSender = bukkitMessageSender;
        this.jsonMessageSender = jsonMessageSender;
    }

    public MessageSender getSender(CommandSender receiver, String message) {
        if(receiver instanceof Player && message.contains("[tooltip|")) {
            return jsonMessageSender;
        }else{
            return bukkitMessageSender;
        }
    }
}

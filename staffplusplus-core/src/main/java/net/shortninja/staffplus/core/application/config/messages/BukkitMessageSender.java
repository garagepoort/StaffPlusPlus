package net.shortninja.staffplus.core.application.config.messages;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

@IocBean
@IocMultiProvider(MessageSender.class)
public class BukkitMessageSender extends AbstractMessageSender {
    @Override
    public void sendMessage(CommandSender receiver, String message, String prefix) {
        for (String s : message.split("\\n")) {
            receiver.sendMessage(buildLine(prefix, s));
        }
    }

    private String buildLine(String prefix, String message) {
        if (StringUtils.isEmpty(prefix)) {
            return colorize(message);
        } else {
            return colorize(prefix + " " + message);
        }
    }

}

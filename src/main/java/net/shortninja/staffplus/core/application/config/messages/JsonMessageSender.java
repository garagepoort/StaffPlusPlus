package net.shortninja.staffplus.core.application.config.messages;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import net.shortninja.staffplus.core.common.JsonSenderService;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Function;

@IocBean
@IocMultiProvider(MessageSender.class)
public class JsonMessageSender extends AbstractMessageSender {

    private final JsonSenderService jsonSenderService;

    public JsonMessageSender(JsonSenderService jsonSenderService) {
        this.jsonSenderService = jsonSenderService;
    }

    @Override
    public void sendMessage(CommandSender receiver, String message, String prefix) {
        if(!(receiver instanceof Player)) {
            throw new BusinessException("receiver for jsonMessage needs to be a player");
        }
        jsonSenderService.send(parseJsonMessage(message, prefix), (Player) receiver);
    }

    public JSONMessage parseJsonMessage(String message, String prefix) {
        if (!StringUtils.isEmpty(prefix)) {
            message = prefix + " " + message;
        }

        String[] messageParts = message.split("(?=(\\[tooltip\\|)(.*?)(]))" + "|" + "(?<=(\\[tooltip\\|)(.{0,1000})(]))");

        JSONMessage jsonMessage = JSONMessage.create();
        for (String messagePart : messageParts) {
            if (messagePart.startsWith("[tooltip|")) {
                String strippedMessage = messagePart.substring(0, messagePart.length() - 1).replace("[tooltip|", "");
                String text = strippedMessage.split("\\|")[0];
                String tooltip = strippedMessage.split("\\|")[1].replace("\\n", "\n");
                addColor(text, jsonMessage::then);
                jsonMessage.tooltip(parseJsonMessage(tooltip, prefix));
            } else {
                addColor(cleanTextPart(messagePart, prefix), jsonMessage::then);
            }
        }
        return jsonMessage;
    }

    private String cleanTextPart(String message, String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return message.replace("\\n", "\n");
        } else {
            return message.replace("\\n", "\n" + prefix + " ");
        }
    }

    private void addColor(String message, Function<String, JSONMessage> jsonMessageFunction) {
        String[] coloredString = message.split("(?=&1|&2|&3|&4|&5|&6|&7|&8|&9|&0|&a|&e|&b|&d|&f|&c|&C)");
        for (String messagePart : coloredString) {
            messagePart = colorize(messagePart);
            jsonMessageFunction.apply(messagePart);
        }
    }
}

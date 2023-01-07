package net.shortninja.staffplus.core.application.config.messages;

import net.shortninja.staffplus.core.common.gui.gradient.GradientColorProcessor;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractMessageSender implements MessageSender{

    private final Pattern hexColorPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    protected String colorize(String message) {
        message = GradientColorProcessor.process(message);
        message = processHexColor(message);
        message = message.replace("&&", "<ampersand>");
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message.replace("<ampersand>", "&");
    }

    @NotNull
    private String processHexColor(String message) {
        Matcher matcher = hexColorPattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = hexColorPattern.matcher(message);
        }
        return message;
    }
}

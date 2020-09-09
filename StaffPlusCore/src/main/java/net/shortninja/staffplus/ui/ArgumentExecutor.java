package net.shortninja.staffplus.ui;

import org.bukkit.command.CommandSender;

public interface ArgumentExecutor {

    boolean execute(CommandSender commandSender, String playerName, String value);

    String getArgsPrefix();
}

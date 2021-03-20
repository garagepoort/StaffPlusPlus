package net.shortninja.staffplus.common.cmd.arguments;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ArgumentTabCompletion {

    List<String> complete(CommandSender sender, String currentArg);
}

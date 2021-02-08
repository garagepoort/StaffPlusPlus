package net.shortninja.staffplus.common.actions;

import net.shortninja.staffplus.player.SppPlayer;
import org.bukkit.command.CommandSender;

public interface ActionFilter {

    boolean isValidAction(CommandSender sender, SppPlayer target, ExecutableAction executableAction);
}

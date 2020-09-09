package net.shortninja.staffplus.common;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface CommandPermissionValidator {

    void validatePermissions(CommandSender commandSender, Player target);
}

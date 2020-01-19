package net.shortninja.staffplus.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface IPermissionsHandler {

    boolean has(Player player, String permission);

    boolean hasOnly(Player player, String permission);

    boolean has(CommandSender sender, String permission);

    boolean isOp(Player player);

    boolean isOp(CommandSender sender);

    int getStaffCount();

}

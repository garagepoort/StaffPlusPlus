package net.shortninja.staffplus.util;

import net.shortninja.staffplus.IocContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permission {
    public boolean has(Player player, String permission) {
        boolean hasPermission = false;

        if (player != null) {
            hasPermission = player.hasPermission(permission) || isOp(player);
        }

        return hasPermission;
    }

    public boolean hasOnly(Player player, String permission) {
        boolean hasPermission = false;

        if (player != null) {
            hasPermission = player.hasPermission(permission) && !player.isOp();
        }

        return hasPermission;
    }

    public boolean has(CommandSender sender, String permission) {
        return sender.hasPermission(permission) || isOp(sender);
    }

    public boolean isOp(Player player) {
        return player.hasPermission(IocContainer.getOptions().permissionWildcard);
    }

    public boolean isOp(CommandSender sender) {
        return sender.hasPermission(IocContainer.getOptions().permissionWildcard);
    }

    public int getStaffCount() {
        int count = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (has(player, IocContainer.getOptions().permissionMember)) {
                count++;
            }
        }

        return count;
    }
}
package net.shortninja.staffplus.core.common.permissions;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;

public class DefaultPermissionHandler implements PermissionHandler {

    @ConfigProperty("permissions:member")
    private String permissionMember;

    public boolean has(Player player, String permission) {
        if (permission == null) {
            return true;
        }

        boolean hasPermission = false;
        if (player != null) {
            hasPermission = player.hasPermission(permission);
        }

        return hasPermission;
    }

    public boolean has(OfflinePlayer player, String permission) {
        return permission == null;
    }

    public boolean hasAny(CommandSender player, String... permissions) {
        return Arrays.stream(permissions).anyMatch(permission -> this.has(player, permission));
    }

    public boolean hasAny(CommandSender player, Set<String> permissions) {
        return permissions.stream().anyMatch(permission -> this.has(player, permission));
    }

    public void validate(CommandSender player, String permission) {
        if (permission != null && !has(player, permission)) {
            throw new NoPermissionException();
        }
    }

    public void validateAny(CommandSender player, Set<String> permissions) {
        if (!permissions.isEmpty() && !hasAny(player, permissions)) {
            throw new NoPermissionException();
        }
    }

    public boolean hasOnly(Player player, String permission) {
        if (permission == null) {
            return true;
        }

        boolean hasPermission = false;
        if (player != null) {
            hasPermission = player.hasPermission(permission);
        }

        return hasPermission;
    }

    public boolean has(CommandSender sender, String permission) {
        if (permission == null) {
            return true;
        }
        return sender.hasPermission(permission);
    }

    public int getStaffCount() {
        int count = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (has(player, permissionMember)) {
                count++;
            }
        }

        return count;
    }

}
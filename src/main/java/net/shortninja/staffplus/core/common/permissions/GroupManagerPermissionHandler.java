package net.shortninja.staffplus.core.common.permissions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.Set;

public class GroupManagerPermissionHandler implements PermissionHandler {

    private final Options options;
    private final GroupManager gMplugin;

    public GroupManagerPermissionHandler(Options options) {
        this.options = options;
        final PluginManager pluginManager = StaffPlus.get().getServer().getPluginManager();
        gMplugin = (GroupManager) pluginManager.getPlugin("GroupManager");
    }

    public boolean has(Player player, String permission) {
        if (permission == null) {
            return true;
        }

        boolean hasPermission = false;
        if (player != null) {
            AnjoPermissionsHandler worldPermissions = gMplugin.getWorldsHolder().getWorldPermissions(player);
            hasPermission = worldPermissions.has(player, permission) || isOp(player);
        }

        return hasPermission;
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
            AnjoPermissionsHandler worldPermissions = gMplugin.getWorldsHolder().getWorldPermissions(player);
            hasPermission = worldPermissions.has(player, permission) && !player.isOp();
        }

        return hasPermission;
    }

    public boolean has(CommandSender sender, String permission) {
        if (permission == null || !(sender instanceof Player)) {
            return true;
        }

        AnjoPermissionsHandler worldPermissions = gMplugin.getWorldsHolder().getWorldPermissions((Player) sender);
        return worldPermissions.has((Player) sender, permission) || isOp(sender);
    }

    @Override
    public int getStaffCount() {
        int count = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (has(player, options.permissionMember)) {
                count++;
            }
        }

        return count;
    }
}

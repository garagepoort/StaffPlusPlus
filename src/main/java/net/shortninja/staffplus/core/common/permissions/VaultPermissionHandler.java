package net.shortninja.staffplus.core.common.permissions;

import net.milkbowl.vault.permission.Permission;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Arrays;
import java.util.Set;

public class VaultPermissionHandler implements PermissionHandler {

    private static Permission perms = null;
    private final Options options;

    public VaultPermissionHandler(Options options) {
        this.options = options;
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            throw new ConfigurationException("Vault plugin was not found. Please disable vault in the config or provide the vault plugin");
        }
        perms = rsp.getProvider();
    }

    public boolean has(Player player, String permission) {
        if (permission == null) {
            return true;
        }

        boolean hasPermission = false;
        if (player != null) {
            hasPermission = perms.has(player, permission) || isOp(player);
        }

        return hasPermission;
    }

    @Override
    public boolean has(OfflinePlayer player, String permission) {
        if (permission == null) {
            return true;
        }

        boolean hasPermission = false;
        if (player != null) {
            World world = Bukkit.getWorlds().stream().filter(w -> w.getName().equalsIgnoreCase(options.mainWorld)).findFirst().orElse(Bukkit.getWorlds().get(0));
            hasPermission = perms.playerHas(world.getName(), player, permission);
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
            hasPermission = perms.has(player, permission) && !player.isOp();
        }

        return hasPermission;
    }

    public boolean has(CommandSender sender, String permission) {
        if (permission == null) {
            return true;
        }
        return perms.has(sender, permission) || isOp(sender);
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

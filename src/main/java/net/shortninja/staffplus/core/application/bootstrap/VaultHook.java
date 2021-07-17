package net.shortninja.staffplus.core.application.bootstrap;

import be.garagepoort.mcioc.IocBean;
import net.milkbowl.vault.permission.Permission;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.permissions.VaultPermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Optional;

@IocBean
public class VaultHook {

    private final Options options;

    public VaultHook(Options options) {
        this.options = options;
    }

    public Optional<PermissionHandler> getVaultPermissionHandler() {
        RegisteredServiceProvider<Permission> registration = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (registration != null) {
            StaffPlus.get().getLogger().info("Vault found. Permissions will be handled by Vault");
            return Optional.of(new VaultPermissionHandler(options));
        }
        return Optional.empty();
    }
}

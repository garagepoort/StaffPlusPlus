package net.shortninja.staffplus.core.application;

import be.garagepoort.mcioc.IocBeanProvider;
import be.garagepoort.mcioc.TubingConfiguration;
import net.milkbowl.vault.permission.Permission;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.DefaultPermissionHandler;
import net.shortninja.staffplus.core.common.permissions.GroupManagerPermissionHandler;
import net.shortninja.staffplus.core.application.bootstrap.LuckPermsHook;
import net.shortninja.staffplus.core.common.permissions.VaultPermissionHandler;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

@TubingConfiguration
public class StaffPlusPlusConfiguration {

    @IocBeanProvider
    public static PermissionHandler instantiatePermissionHandler(Options options) {
        final PluginManager pluginManager = StaffPlus.get().getServer().getPluginManager();
        Plugin gMplugin = pluginManager.getPlugin("GroupManager");
        if(gMplugin != null && gMplugin.isEnabled()) {
            StaffPlus.get().getLogger().info("GroupManager found. Permissions will be handled by GroupManager");
            return new GroupManagerPermissionHandler(options);
        }

        RegisteredServiceProvider<Permission> registration = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (registration != null) {
            StaffPlus.get().getLogger().info("Vault found. Permissions will be handled by Vault");
            return new VaultPermissionHandler(options);
        }

        StaffPlus.get().getLogger().info("Permissions handled by Bukkit");
        return new DefaultPermissionHandler(options);
    }

    @IocBeanProvider
    public static LuckPermsHook instantiateLuckperms(SessionManagerImpl sessionManager) {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            return new LuckPermsHook(sessionManager);
        }
        StaffPlus.get().getLogger().info("Luckperms not found. Not Setting luckperms hook");
        return null;
    }
}

package net.shortninja.staffplus.core.application;

import be.garagepoort.mcioc.IocBeanProvider;
import be.garagepoort.mcioc.TubingConfiguration;
import net.milkbowl.vault.permission.Permission;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.permissions.DefaultPermissionHandler;
import net.shortninja.staffplus.core.common.permissions.GroupManagerPermissionHandler;
import net.shortninja.staffplus.core.application.bootstrap.LuckPermsHook;
import net.shortninja.staffplus.core.common.permissions.VaultPermissionHandler;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

@TubingConfiguration
public class StaffPlusPlusConfiguration {

    @IocBeanProvider
    public static Class<? extends PermissionHandler> instantiatePermissionHandler() {
        final PluginManager pluginManager = StaffPlus.get().getServer().getPluginManager();
        Plugin gMplugin = pluginManager.getPlugin("GroupManager");
        if(gMplugin != null && gMplugin.isEnabled()) {
            StaffPlus.get().getLogger().info("GroupManager found. Permissions will be handled by GroupManager");
            return GroupManagerPermissionHandler.class;
        }

        RegisteredServiceProvider<Permission> registration = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (registration.getProvider() != null) {
            StaffPlus.get().getLogger().info("Vault found. Permissions will be handled by Vault");
            return VaultPermissionHandler.class;
        }

        StaffPlus.get().getLogger().info("Permissions handled by Bukkit");
        return DefaultPermissionHandler.class;
    }

    @IocBeanProvider
    public static Class<LuckPermsHook> instantiateLuckperms() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            return LuckPermsHook.class;
        }
        StaffPlus.get().getLogger().info("Luckperms not found. Not Setting luckperms hook");
        return null;
    }
}

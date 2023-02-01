package net.shortninja.staffplus.core.application;

import be.garagepoort.mcioc.AfterIocLoad;
import be.garagepoort.mcioc.IocBeanProvider;
import be.garagepoort.mcioc.TubingConfiguration;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import be.garagepoort.mcioc.tubinggui.exceptions.GuiExceptionHandler;
import be.garagepoort.mcsqlmigrations.DatabaseType;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.bootstrap.VaultHook;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.DefaultPermissionHandler;
import net.shortninja.staffplus.core.common.permissions.GroupManagerPermissionHandler;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Optional;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

@TubingConfiguration
public class StaffPlusPlusConfiguration {

    @AfterIocLoad
    public static void initGuiExceptionHandler(GuiActionService guiActionService) {
        guiActionService.registerExceptionHandler(BusinessException.class, (GuiExceptionHandler<BusinessException>) (player, e) -> player.sendMessage(translateAlternateColorCodes('&', e.getMessage())));
    }

    @IocBeanProvider
    public static PermissionHandler instantiatePermissionHandler(Options options, VaultHook vaultHook) {
        final PluginManager pluginManager = TubingBukkitPlugin.getPlugin().getServer().getPluginManager();
        Plugin gMplugin = pluginManager.getPlugin("GroupManager");
        if (gMplugin != null && gMplugin.isEnabled()) {
            TubingBukkitPlugin.getPlugin().getLogger().info("GroupManager found. Permissions will be handled by GroupManager");
            return new GroupManagerPermissionHandler(options);
        }

        try {
            Class.forName("net.milkbowl.vault.permission.Permission", false, StaffPlusPlusConfiguration.class.getClassLoader());
            Optional<PermissionHandler> vaultPermissionHandler = vaultHook.getVaultPermissionHandler();
            if (vaultPermissionHandler.isPresent()) {
                return vaultPermissionHandler.get();
            }
        } catch (ClassNotFoundException e) {
            // Vault not found
        }

        TubingBukkitPlugin.getPlugin().getLogger().info("Permissions handled by Bukkit");
        return new DefaultPermissionHandler();
    }

    @IocBeanProvider
    public static QueryBuilderFactory queryBuilderFactory(@ConfigProperty("storage.type") String storageType, SqlConnectionProvider sqlConnectionProvider) {
        if (storageType.equalsIgnoreCase("mysql")) {
            TubingBukkitPlugin.getPlugin().getLogger().info("Using MYSQL storage");
            return new QueryBuilderFactory(DatabaseType.MYSQL, sqlConnectionProvider);
        }

        TubingBukkitPlugin.getPlugin().getLogger().info("Using SQLITE storage");
        return new QueryBuilderFactory(DatabaseType.SQLITE, sqlConnectionProvider);
    }
}

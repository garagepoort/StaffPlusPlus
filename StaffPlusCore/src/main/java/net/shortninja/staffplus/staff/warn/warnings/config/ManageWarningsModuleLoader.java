package net.shortninja.staffplus.staff.warn.warnings.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class ManageWarningsModuleLoader extends ConfigLoader<ManageWarningsConfiguration> {

    @Override
    protected ManageWarningsConfiguration load(FileConfiguration config) {
        String commandManageWarningsGui = config.getString("commands.warnings.manage.gui");
        String commandManageAppealedWarningsGui = config.getString("commands.warnings.manage.appealed-gui");
        String permissionManageWarningsView = config.getString("permissions.warnings.manage.view");
        String permissionManageWarningsDelete = config.getString("permissions.warnings.manage.delete");

        return new ManageWarningsConfiguration(
            commandManageWarningsGui,
            commandManageAppealedWarningsGui, permissionManageWarningsView,
            permissionManageWarningsDelete);
    }
}

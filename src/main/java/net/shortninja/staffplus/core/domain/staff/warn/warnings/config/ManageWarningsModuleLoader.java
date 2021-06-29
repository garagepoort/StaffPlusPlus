package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;

@IocBean
public class ManageWarningsModuleLoader extends AbstractConfigLoader<ManageWarningsConfiguration> {

    @Override
    protected ManageWarningsConfiguration load() {
        String commandManageWarningsGui = commandsConfig.getString("commands.warnings.manage.gui");
        String commandManageAppealedWarningsGui = commandsConfig.getString("commands.warnings.manage.appealed-gui");
        String permissionManageWarningsView = permissionsConfig.getString("permissions.warnings.manage.view");
        String permissionManageWarningsDelete = permissionsConfig.getString("permissions.warnings.manage.delete");
        String permissionManageWarningsExpire = permissionsConfig.getString("permissions.warnings.manage.expire");

        return new ManageWarningsConfiguration(
            commandManageWarningsGui,
            commandManageAppealedWarningsGui, permissionManageWarningsView,
            permissionManageWarningsDelete,
            permissionManageWarningsExpire);
    }
}

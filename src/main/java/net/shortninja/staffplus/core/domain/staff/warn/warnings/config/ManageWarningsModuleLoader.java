package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class ManageWarningsModuleLoader extends AbstractConfigLoader<ManageWarningsConfiguration> {

    @Override
    protected ManageWarningsConfiguration load() {
        String commandManageWarningsGui = defaultConfig.getString("commands.warnings.manage.gui");
        String commandManageAppealedWarningsGui = defaultConfig.getString("commands.warnings.manage.appealed-gui");
        String permissionManageWarningsView = defaultConfig.getString("permissions.warnings.manage.view");
        String permissionManageWarningsDelete = defaultConfig.getString("permissions.warnings.manage.delete");
        String permissionManageWarningsExpire = defaultConfig.getString("permissions.warnings.manage.expire");

        return new ManageWarningsConfiguration(
            commandManageWarningsGui,
            commandManageAppealedWarningsGui, permissionManageWarningsView,
            permissionManageWarningsDelete,
            permissionManageWarningsExpire);
    }
}

package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class ManageWarningsConfiguration {

    @ConfigProperty("commands:commands.warnings.manage.gui")
    public String commandManageWarningsGui;
    @ConfigProperty("commands:commands.warnings.manage.appealed-gui")
    public String commandManageAppealedWarningsGui;
    @ConfigProperty("permissions:permissions.warnings.manage.view")
    public String permissionView;
    @ConfigProperty("permissions:permissions.warnings.manage.delete")
    public String permissionDelete;
    @ConfigProperty("permissions:permissions.warnings.manage.expire")
    public String permissionExpire;

}

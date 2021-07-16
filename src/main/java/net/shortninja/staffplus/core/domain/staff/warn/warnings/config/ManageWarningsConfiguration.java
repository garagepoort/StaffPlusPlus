package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class ManageWarningsConfiguration {

    @ConfigProperty("commands:warnings.manage.gui")
    public String commandManageWarningsGui;
    @ConfigProperty("commands:warnings.manage.appealed-gui")
    public String commandManageAppealedWarningsGui;
    @ConfigProperty("permissions:warnings.manage.view")
    public String permissionView;
    @ConfigProperty("permissions:warnings.manage.delete")
    public String permissionDelete;
    @ConfigProperty("permissions:warnings.manage.expire")
    public String permissionExpire;

}

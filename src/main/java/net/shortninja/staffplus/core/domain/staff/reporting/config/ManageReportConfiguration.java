package net.shortninja.staffplus.core.domain.staff.reporting.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class ManageReportConfiguration {

    @ConfigProperty("commands:commands.reports.manage.gui")
    public String commandManageReportsGui;
    @ConfigProperty("permissions:permissions.reports.manage.view")
    public String permissionView;
    @ConfigProperty("permissions:permissions.reports.manage.view")
    public String permissionDelete;
    @ConfigProperty("permissions:permissions.reports.manage.delete")
    public String permissionAccept;
    @ConfigProperty("permissions:permissions.reports.manage.resolve")
    public String permissionResolve;
    @ConfigProperty("permissions:permissions.reports.manage.reject")
    public String permissionReject;
    @ConfigProperty("permissions:permissions.reports.manage.teleport")
    public String permissionTeleport;
    @ConfigProperty("permissions:permissions.reports.manage.reopen-other")
    public String permissionReopenOther;
}

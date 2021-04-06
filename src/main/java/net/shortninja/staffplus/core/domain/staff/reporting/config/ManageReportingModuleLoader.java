package net.shortninja.staffplus.core.domain.staff.reporting.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class ManageReportingModuleLoader extends AbstractConfigLoader<ManageReportConfiguration> {

    @Override
    protected ManageReportConfiguration load() {
        String commandManageReportsGui = defaultConfig.getString("commands.reports.manage.gui");
        String permissionManageReportsView = defaultConfig.getString("permissions.reports.manage.view");
        String permissionManageReportsDelete = defaultConfig.getString("permissions.reports.manage.delete");
        String permissionManageReportsAccept = defaultConfig.getString("permissions.reports.manage.accept");
        String permissionManageReportsResolve = defaultConfig.getString("permissions.reports.manage.resolve");
        String permissionManageReportsReject = defaultConfig.getString("permissions.reports.manage.reject");
        String permissionManageReportsTeleport = defaultConfig.getString("permissions.reports.manage.teleport");
        String permissionManageReportsReopenOther = defaultConfig.getString("permissions.reports.manage.reopen-other");

        return new ManageReportConfiguration(
            commandManageReportsGui,
            permissionManageReportsView,
            permissionManageReportsDelete,
            permissionManageReportsAccept,
            permissionManageReportsResolve,
            permissionManageReportsReject,
            permissionManageReportsTeleport,
            permissionManageReportsReopenOther);
    }
}

package net.shortninja.staffplus.core.domain.staff.reporting.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;

@IocBean
public class ManageReportingModuleLoader extends AbstractConfigLoader<ManageReportConfiguration> {

    @Override
    protected ManageReportConfiguration load() {
        String commandManageReportsGui = commandsConfig.getString("commands.reports.manage.gui");
        String permissionManageReportsView = permissionsConfig.getString("permissions.reports.manage.view");
        String permissionManageReportsDelete = permissionsConfig.getString("permissions.reports.manage.delete");
        String permissionManageReportsAccept = permissionsConfig.getString("permissions.reports.manage.accept");
        String permissionManageReportsResolve = permissionsConfig.getString("permissions.reports.manage.resolve");
        String permissionManageReportsReject = permissionsConfig.getString("permissions.reports.manage.reject");
        String permissionManageReportsTeleport = permissionsConfig.getString("permissions.reports.manage.teleport");
        String permissionManageReportsReopenOther = permissionsConfig.getString("permissions.reports.manage.reopen-other");

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

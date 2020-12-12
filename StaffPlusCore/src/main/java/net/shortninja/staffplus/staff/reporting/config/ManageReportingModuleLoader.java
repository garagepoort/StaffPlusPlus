package net.shortninja.staffplus.staff.reporting.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class ManageReportingModuleLoader extends ConfigLoader<ManageReportConfiguration> {

    @Override
    protected ManageReportConfiguration load(FileConfiguration config) {
        String commandManageReportsGui = config.getString("commands.reports.manage.gui");
        String permissionManageReportsView = config.getString("permissions.reports.manage.view");
        String permissionManageReportsDelete = config.getString("permissions.reports.manage.delete");
        String permissionManageReportsAccept = config.getString("permissions.reports.manage.accept");
        String permissionManageReportsResolve = config.getString("permissions.reports.manage.resolve");
        String permissionManageReportsReject = config.getString("permissions.reports.manage.reject");

        return new ManageReportConfiguration(
            commandManageReportsGui,
            permissionManageReportsView,
            permissionManageReportsDelete,
            permissionManageReportsAccept,
            permissionManageReportsResolve,
            permissionManageReportsReject
        );
    }
}

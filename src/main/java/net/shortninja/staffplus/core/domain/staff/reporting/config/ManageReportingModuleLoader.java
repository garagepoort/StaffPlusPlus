package net.shortninja.staffplus.core.domain.staff.reporting.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class ManageReportingModuleLoader extends AbstractConfigLoader<ManageReportConfiguration> {

    @Override
    protected ManageReportConfiguration load(FileConfiguration config) {
        String commandManageReportsGui = config.getString("commands.reports.manage.gui");
        String permissionManageReportsView = config.getString("permissions.reports.manage.view");
        String permissionManageReportsDelete = config.getString("permissions.reports.manage.delete");
        String permissionManageReportsAccept = config.getString("permissions.reports.manage.accept");
        String permissionManageReportsResolve = config.getString("permissions.reports.manage.resolve");
        String permissionManageReportsReject = config.getString("permissions.reports.manage.reject");
        String permissionManageReportsTeleport = config.getString("permissions.reports.manage.teleport");
        String permissionManageReportsReopenOther = config.getString("permissions.reports.manage.reopen-other");

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

package net.shortninja.staffplus.core.domain.staff.warn.appeals.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@IocBean
public class AppealModuleLoader extends AbstractConfigLoader<AppealConfiguration> {

    @Override
    protected AppealConfiguration load(FileConfiguration config) {
        boolean enabled = config.getBoolean("warnings-module.appeals.enabled");
        boolean fixedReason = config.getBoolean("warnings-module.appeals.fixed-reason");
        boolean resolveReasonEnable = config.getBoolean("warnings-module.appeals.resolve-reason-enabled");
        List<String> reasons = (List<String>) config.getList("warnings-module.appeals.reasons", new ArrayList<>());
        String permissionCreateAppeal = config.getString("permissions.warnings.appeals.create");
        String permissionCreateOthersAppeal = config.getString("permissions.warnings.appeals.create-others");
        String permissionApproveAppeal = config.getString("permissions.warnings.appeals.approve");
        String permissionRejectAppeal = config.getString("permissions.warnings.appeals.reject");
        String permissionNotifications = config.getString("permissions.warnings.appeals.notifications");


        return new AppealConfiguration(enabled, resolveReasonEnable, permissionApproveAppeal, permissionRejectAppeal, permissionCreateAppeal, permissionCreateOthersAppeal, fixedReason, reasons, permissionNotifications);
    }

}

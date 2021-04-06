package net.shortninja.staffplus.core.domain.staff.warn.appeals.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import java.util.ArrayList;
import java.util.List;

@IocBean
public class AppealModuleLoader extends AbstractConfigLoader<AppealConfiguration> {

    @Override
    protected AppealConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("warnings-module.appeals.enabled");
        boolean fixedReason = defaultConfig.getBoolean("warnings-module.appeals.fixed-reason");
        boolean resolveReasonEnable = defaultConfig.getBoolean("warnings-module.appeals.resolve-reason-enabled");
        List<String> reasons = (List<String>) defaultConfig.getList("warnings-module.appeals.reasons", new ArrayList<>());
        String permissionCreateAppeal = defaultConfig.getString("permissions.warnings.appeals.create");
        String permissionCreateOthersAppeal = defaultConfig.getString("permissions.warnings.appeals.create-others");
        String permissionApproveAppeal = defaultConfig.getString("permissions.warnings.appeals.approve");
        String permissionRejectAppeal = defaultConfig.getString("permissions.warnings.appeals.reject");
        String permissionNotifications = defaultConfig.getString("permissions.warnings.appeals.notifications");


        return new AppealConfiguration(enabled, resolveReasonEnable, permissionApproveAppeal, permissionRejectAppeal, permissionCreateAppeal, permissionCreateOthersAppeal, fixedReason, reasons, permissionNotifications);
    }

}

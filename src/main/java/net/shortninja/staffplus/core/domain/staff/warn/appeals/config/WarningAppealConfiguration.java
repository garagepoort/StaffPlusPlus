package net.shortninja.staffplus.core.domain.staff.warn.appeals.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

import java.util.List;

@IocBean
public class WarningAppealConfiguration {

    @ConfigProperty("warnings-module.appeals.enabled")
    public boolean enabled;
    @ConfigProperty("warnings-module.appeals.resolve-reason-enabled")
    public boolean resolveReasonEnabled;

    @ConfigProperty("permissions:warnings.appeals.approve")
    public String approveAppealPermission;
    @ConfigProperty("permissions:warnings.appeals.reject")
    public String rejectAppealPermission;
    @ConfigProperty("permissions:warnings.appeals.create")
    public String createAppealPermission;
    @ConfigProperty("permissions:warnings.appeals.create-others")
    public String permissionCreateOthersAppeal;
    @ConfigProperty("permissions:warnings.appeals.notifications")
    public String permissionNotifications;

    @ConfigProperty("warnings-module.appeals.fixed-reason")
    public boolean fixedAppealReason;
    @ConfigProperty("warnings-module.appeals.reasons")
    public List<String> appealReasons;

}

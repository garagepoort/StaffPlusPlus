package net.shortninja.staffplus.core.domain.staff.ban.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

import java.util.List;

@IocBean
public class BanAppealConfiguration {

    @ConfigProperty("ban-module.appeals.enabled")
    public boolean enabled;
    @ConfigProperty("ban-module.appeals.resolve-reason-enabled")
    public boolean resolveReasonEnabled;

    @ConfigProperty("permissions:bans.appeals.approve")
    public String approveAppealPermission;
    @ConfigProperty("permissions:bans.appeals.reject")
    public String rejectAppealPermission;
    @ConfigProperty("permissions:bans.appeals.create")
    public String createAppealPermission;
    @ConfigProperty("permissions:bans.appeals.create-others")
    public String permissionCreateOthersAppeal;
    @ConfigProperty("permissions:bans.appeals.notifications")
    public String permissionNotifications;

    @ConfigProperty("ban-module.appeals.unban-on-approve")
    public boolean unbanOnApprove;
    @ConfigProperty("ban-module.appeals.fixed-reason")
    public boolean fixedAppealReason;
    @ConfigProperty("ban-module.appeals.reasons")
    public List<String> appealReasons;

}

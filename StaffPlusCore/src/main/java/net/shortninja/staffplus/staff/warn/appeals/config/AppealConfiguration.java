package net.shortninja.staffplus.staff.warn.appeals.config;

import java.util.List;

public class AppealConfiguration {

    private final boolean enabled;
    private final boolean resolveReasonEnable;
    private final String approveAppealPermission;
    private final String rejectAppealPermission;
    private final String createAppealPermission;
    private final boolean fixedAppealReason;
    private final List<String> appealReasons;

    public AppealConfiguration(boolean enabled, boolean resolveReasonEnable, String approveAppealPermission,String rejectAppealPermission, String createAppealPermission, boolean fixedAppealReason, List<String> appealReasons) {
        this.enabled = enabled;
        this.resolveReasonEnable = resolveReasonEnable;
        this.approveAppealPermission = approveAppealPermission;
        this.rejectAppealPermission = rejectAppealPermission;
        this.createAppealPermission = createAppealPermission;
        this.fixedAppealReason = fixedAppealReason;
        this.appealReasons = appealReasons;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isResolveReasonEnabled() {
        return resolveReasonEnable;
    }

    public String getApproveAppealPermission() {
        return approveAppealPermission;
    }

    public String getRejectAppealPermission() {
        return rejectAppealPermission;
    }

    public String getCreateAppealPermission() {
        return createAppealPermission;
    }

    public boolean isFixedAppealReason() {
        return fixedAppealReason;
    }

    public List<String> getAppealReasons() {
        return appealReasons;
    }
}

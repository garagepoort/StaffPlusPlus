package net.shortninja.staffplus.core.domain.staff.warn.appeals.config;

import java.util.List;

public class AppealConfiguration {

    private final boolean enabled;
    private final boolean resolveReasonEnabled;
    private final String approveAppealPermission;
    private final String rejectAppealPermission;
    private final String createAppealPermission;
    private final String permissionCreateOthersAppeal;
    private final boolean fixedAppealReason;
    private final List<String> appealReasons;
    private final String permissionNotifications;

    public AppealConfiguration(boolean enabled, boolean resolveReasonEnabled, String approveAppealPermission,
                               String rejectAppealPermission,
                               String createAppealPermission, String permissionCreateOthersAppeal,
                               boolean fixedAppealReason, List<String> appealReasons,
                               String permissionNotifications) {
        this.enabled = enabled;
        this.resolveReasonEnabled = resolveReasonEnabled;
        this.approveAppealPermission = approveAppealPermission;
        this.rejectAppealPermission = rejectAppealPermission;
        this.createAppealPermission = createAppealPermission;
        this.permissionCreateOthersAppeal = permissionCreateOthersAppeal;
        this.fixedAppealReason = fixedAppealReason;
        this.appealReasons = appealReasons;
        this.permissionNotifications = permissionNotifications;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isResolveReasonEnabled() {
        return resolveReasonEnabled;
    }

    public String getApproveAppealPermission() {
        return approveAppealPermission;
    }

    public String getRejectAppealPermission() {
        return rejectAppealPermission;
    }

    public String[] getCreateAppealPermissions() {
        return new String[]{createAppealPermission, permissionCreateOthersAppeal};
    }

    public boolean isFixedAppealReason() {
        return fixedAppealReason;
    }

    public List<String> getAppealReasons() {
        return appealReasons;
    }

    public String getPermissionNotifications() {
        return permissionNotifications;
    }

    public String getCreateAppealPermission() {
        return createAppealPermission;
    }

    public String getCreateOthersAppealPermission() {
        return permissionCreateOthersAppeal;
    }
}

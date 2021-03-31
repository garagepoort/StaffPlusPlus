package net.shortninja.staffplus.core.domain.synchronization;

public class ServerSyncConfiguration {

    private boolean vanishSyncEnabled;
    private boolean staffModeSyncEnabled;
    private boolean banSyncEnabled;
    private boolean reportSyncEnabled;
    private boolean warningSyncEnabled;
    private boolean muteSyncEnabled;
    private boolean kickSyncEnabled;
    private boolean investigationSyncEnabled;

    public ServerSyncConfiguration(boolean vanishSyncEnabled, boolean staffModeSyncEnabled, boolean banSyncEnabled, boolean reportSyncEnabled, boolean warningSyncEnabled, boolean muteSyncEnabled, boolean kickSyncEnabled, boolean investigationSyncEnabled) {
        this.vanishSyncEnabled = vanishSyncEnabled;
        this.staffModeSyncEnabled = staffModeSyncEnabled;
        this.banSyncEnabled = banSyncEnabled;
        this.reportSyncEnabled = reportSyncEnabled;
        this.warningSyncEnabled = warningSyncEnabled;
        this.muteSyncEnabled = muteSyncEnabled;
        this.kickSyncEnabled = kickSyncEnabled;
        this.investigationSyncEnabled = investigationSyncEnabled;
    }

    public boolean isVanishSyncEnabled() {
        return vanishSyncEnabled;
    }

    public boolean isStaffModeSyncEnabled() {
        return staffModeSyncEnabled;
    }

    public boolean isBanSyncEnabled() {
        return banSyncEnabled;
    }

    public boolean isReportSyncEnabled() {
        return reportSyncEnabled;
    }

    public boolean isWarningSyncEnabled() {
        return warningSyncEnabled;
    }

    public boolean isMuteSyncEnabled() {
        return muteSyncEnabled;
    }

    public boolean isKickSyncEnabled() {
        return kickSyncEnabled;
    }

    public boolean sessionSyncEnabled() {
        return vanishSyncEnabled || staffModeSyncEnabled;
    }

    public boolean isInvestigationSyncEnabled() {
        return investigationSyncEnabled;
    }
}

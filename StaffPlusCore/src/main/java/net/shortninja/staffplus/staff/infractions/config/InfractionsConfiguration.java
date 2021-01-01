package net.shortninja.staffplus.staff.infractions.config;

public class InfractionsConfiguration {

    private final boolean infractionsEnabled;

    private final String commandOpenGui;
    private final String permissionViewInfractions;
    private final boolean showBans;
    private final boolean showMutes;
    private final boolean showWarnings;
    private final boolean showReported;
    private final boolean showKicks;

    public InfractionsConfiguration(boolean infractionsEnabled, String commandOpenGui, String permissionViewInfractions, boolean showBans, boolean showMutes, boolean showWarnings, boolean showReported, boolean showKicks) {
        this.infractionsEnabled = infractionsEnabled;
        this.commandOpenGui = commandOpenGui;
        this.permissionViewInfractions = permissionViewInfractions;
        this.showBans = showBans;
        this.showMutes = showMutes;
        this.showWarnings = showWarnings;
        this.showReported = showReported;
        this.showKicks = showKicks;
    }

    public boolean isEnabled() {
        return infractionsEnabled;
    }

    public String getCommandOpenGui() {
        return commandOpenGui;
    }

    public boolean isShowBans() {
        return showBans;
    }

    public boolean isShowMutes() {
        return showMutes;
    }

    public boolean isShowWarnings() {
        return showWarnings;
    }

    public boolean isShowReported() {
        return showReported;
    }

    public String getPermissionViewInfractions() {
        return permissionViewInfractions;
    }

    public boolean isShowKicks() {
        return showKicks;
    }
}

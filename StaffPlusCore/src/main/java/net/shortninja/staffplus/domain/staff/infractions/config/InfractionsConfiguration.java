package net.shortninja.staffplus.domain.staff.infractions.config;

import org.bukkit.Material;

public class InfractionsConfiguration {

    private final boolean infractionsEnabled;

    private final String commandOpenGui;
    private final String commandOpenTopGui;
    private final String permissionViewInfractions;
    private final boolean showBans;
    private final boolean showMutes;
    private final boolean showWarnings;
    private final boolean showReported;
    private final boolean showKicks;
    private final Material bansGuiItem;
    private final Material mutesGuiItem;
    private final Material warningsGuiItem;
    private final Material reportedGuiItem;
    private final Material kicksGuiItem;

    public InfractionsConfiguration(boolean infractionsEnabled, String commandOpenGui, String commandOpenTopGui, String permissionViewInfractions,
                                    boolean showBans, boolean showMutes, boolean showWarnings, boolean showReported, boolean showKicks,
                                    Material bansGuiItem, Material mutesGuiItem, Material warningsGuiItem, Material reportedGuiItem, Material kicksGuiItem) {
        this.infractionsEnabled = infractionsEnabled;
        this.commandOpenGui = commandOpenGui;
        this.commandOpenTopGui = commandOpenTopGui;
        this.permissionViewInfractions = permissionViewInfractions;
        this.showBans = showBans;
        this.showMutes = showMutes;
        this.showWarnings = showWarnings;
        this.showReported = showReported;
        this.showKicks = showKicks;
        this.bansGuiItem = bansGuiItem;
        this.mutesGuiItem = mutesGuiItem;
        this.warningsGuiItem = warningsGuiItem;
        this.reportedGuiItem = reportedGuiItem;
        this.kicksGuiItem = kicksGuiItem;
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

    public String getCommandOpenTopGui() {
        return commandOpenTopGui;
    }

    public Material getBansGuiItem() {
        return bansGuiItem;
    }

    public Material getMutesGuiItem() {
        return mutesGuiItem;
    }

    public Material getWarningsGuiItem() {
        return warningsGuiItem;
    }

    public Material getReportedGuiItem() {
        return reportedGuiItem;
    }

    public Material getKicksGuiItem() {
        return kicksGuiItem;
    }
}

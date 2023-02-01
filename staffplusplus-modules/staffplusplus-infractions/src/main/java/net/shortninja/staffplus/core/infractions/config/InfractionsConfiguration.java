package net.shortninja.staffplus.core.infractions.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToEnum;
import org.bukkit.Material;

@IocBean
public class InfractionsConfiguration {

    @ConfigProperty("infractions-module.show-bans")
    private boolean showBans;
    @ConfigProperty("infractions-module.show-mutes")
    private boolean showMutes;
    @ConfigProperty("infractions-module.show-warnings")
    private boolean showWarnings;
    @ConfigProperty("infractions-module.show-reported")
    private boolean showReported;
    @ConfigProperty("infractions-module.show-kicks")
    private boolean showKicks;

    @ConfigProperty("infractions-module.bans-gui-item")
    @ConfigTransformer(ToEnum.class)
    private Material bansGuiItem;
    @ConfigProperty("infractions-module.mutes-gui-item")
    @ConfigTransformer(ToEnum.class)
    private Material mutesGuiItem;
    @ConfigProperty("infractions-module.warnings-gui-item")
    @ConfigTransformer(ToEnum.class)
    private Material warningsGuiItem;
    @ConfigProperty("infractions-module.kicks-gui-item")
    @ConfigTransformer(ToEnum.class)
    private Material kicksGuiItem;

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

    public boolean isShowKicks() {
        return showKicks;
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

    public Material getKicksGuiItem() {
        return kicksGuiItem;
    }
}

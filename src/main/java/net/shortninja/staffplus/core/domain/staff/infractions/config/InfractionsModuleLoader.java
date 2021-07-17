package net.shortninja.staffplus.core.domain.staff.infractions.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import org.bukkit.Material;

@IocBean
public class InfractionsModuleLoader extends AbstractConfigLoader<InfractionsConfiguration> {

    @Override
    protected InfractionsConfiguration load() {
        boolean infractionsEnabled = defaultConfig.getBoolean("infractions-module.enabled");
        boolean showBans = defaultConfig.getBoolean("infractions-module.show-bans");
        boolean showMutes = defaultConfig.getBoolean("infractions-module.show-mutes");
        boolean showWarnings = defaultConfig.getBoolean("infractions-module.show-warnings");
        boolean showReported = defaultConfig.getBoolean("infractions-module.show-reported");
        boolean showKicks = defaultConfig.getBoolean("infractions-module.show-kicks");

        Material bansGuiItem = Material.valueOf(defaultConfig.getString("infractions-module.bans-gui-item"));
        Material mutesGuiItem = Material.valueOf(defaultConfig.getString("infractions-module.mutes-gui-item"));
        Material warningsGuiItem = Material.valueOf(defaultConfig.getString("infractions-module.warnings-gui-item"));
        Material reportedGuiItem = Material.valueOf(defaultConfig.getString("infractions-module.reported-gui-item"));
        Material kicksGuiItem = Material.valueOf(defaultConfig.getString("infractions-module.kicks-gui-item"));

        String commandViewTopInfraction = commandsConfig.getString("infractions-top-view");
        String commandViewInfraction = commandsConfig.getString("infractions-view");
        String permissionViewInfractions = permissionsConfig.getString("infractions.view");

        return new InfractionsConfiguration(infractionsEnabled, commandViewInfraction, commandViewTopInfraction, permissionViewInfractions,
            showBans,
            showMutes,
            showWarnings,
            showReported,
            showKicks,
            bansGuiItem,
            mutesGuiItem,
            warningsGuiItem,
            reportedGuiItem,
            kicksGuiItem);
    }
}

package net.shortninja.staffplus.core.domain.staff.infractions.config;

import net.shortninja.staffplus.core.common.config.ConfigLoader;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class InfractionsModuleLoader extends ConfigLoader<InfractionsConfiguration> {

    @Override
    protected InfractionsConfiguration load(FileConfiguration config) {
        boolean infractionsEnabled = config.getBoolean("infractions-module.enabled");
        boolean showBans = config.getBoolean("infractions-module.show-bans");
        boolean showMutes = config.getBoolean("infractions-module.show-mutes");
        boolean showWarnings = config.getBoolean("infractions-module.show-warnings");
        boolean showReported = config.getBoolean("infractions-module.show-reported");
        boolean showKicks = config.getBoolean("infractions-module.show-kicks");

        Material bansGuiItem = Material.valueOf(config.getString("infractions-module.bans-gui-item"));
        Material mutesGuiItem = Material.valueOf(config.getString("infractions-module.mutes-gui-item"));
        Material warningsGuiItem = Material.valueOf(config.getString("infractions-module.warnings-gui-item"));
        Material reportedGuiItem = Material.valueOf(config.getString("infractions-module.reported-gui-item"));
        Material kicksGuiItem = Material.valueOf(config.getString("infractions-module.kicks-gui-item"));

        String commandViewTopInfraction = config.getString("commands.infractions-top-view");
        String commandViewInfraction = config.getString("commands.infractions-view");
        String permissionViewInfractions = config.getString("permissions.infractions.view");

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

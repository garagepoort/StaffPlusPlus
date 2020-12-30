package net.shortninja.staffplus.staff.infractions.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class InfractionsModuleLoader extends ConfigLoader<InfractionsConfiguration> {

    @Override
    protected InfractionsConfiguration load(FileConfiguration config) {
        boolean infractionsEnabled = config.getBoolean("infractions-module.enabled");
        boolean showBans = config.getBoolean("infractions-module.show-bans");
        boolean showMutes = config.getBoolean("infractions-module.show-mutes");
        boolean showWarnings = config.getBoolean("infractions-module.show-warnings");
        boolean showReported = config.getBoolean("infractions-module.show-reported");


        String commandViewInfraction = config.getString("commands.infractions-view");
        String permissionViewInfractions = config.getString("permissions.infractions.view");

        return new InfractionsConfiguration(infractionsEnabled, commandViewInfraction, permissionViewInfractions, showBans, showMutes, showWarnings, showReported);
    }
}

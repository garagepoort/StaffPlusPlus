package net.shortninja.staffplus.domain.staff.examine.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class ExamineModuleLoader extends ConfigLoader<ExamineConfiguration> {

    @Override
    protected ExamineConfiguration load(FileConfiguration config) {
        String permissionExamine = config.getString("permissions.examine");
        String permissionExamineInventoryInteraction = config.getString("permissions.examine-inventory-interaction.online");
        String permissionExamineInventoryInteractionOffline = config.getString("permissions.examine-inventory-interaction.offline");
        String permissionExamineViewInventory = config.getString("permissions.examine-view-inventory.online");
        String permissionExamineViewInventoryOffline = config.getString("permissions.examine-view-inventory.offline");
        String commandExamine = config.getString("commands.examine");

        return new ExamineConfiguration(permissionExamine, permissionExamineInventoryInteraction, permissionExamineInventoryInteractionOffline, permissionExamineViewInventory, permissionExamineViewInventoryOffline, commandExamine );
    }
}

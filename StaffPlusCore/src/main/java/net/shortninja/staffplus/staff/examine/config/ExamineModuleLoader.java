package net.shortninja.staffplus.staff.examine.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class ExamineModuleLoader extends ConfigLoader<ExamineConfiguration> {

    @Override
    protected ExamineConfiguration load(FileConfiguration config) {
        String permissionExamine = config.getString("permissions.examine");
        String permissionExamineInventoryInteraction = config.getString("permissions.examine-inventory-interaction");
        String permissionExamineViewInventory = config.getString("permissions.examine-view-inventory");
        String commandExamine = config.getString("commands.examine");

        return new ExamineConfiguration(permissionExamine, permissionExamineInventoryInteraction, permissionExamineViewInventory, commandExamine );
    }
}

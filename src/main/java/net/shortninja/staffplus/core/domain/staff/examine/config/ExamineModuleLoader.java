package net.shortninja.staffplus.core.domain.staff.examine.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class ExamineModuleLoader extends AbstractConfigLoader<ExamineConfiguration> {

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

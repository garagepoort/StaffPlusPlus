package net.shortninja.staffplus.core.domain.staff.examine.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class ExamineModuleLoader extends AbstractConfigLoader<ExamineConfiguration> {

    @Override
    protected ExamineConfiguration load() {
        String permissionExamine = defaultConfig.getString("permissions.examine");
        String permissionExamineInventoryInteraction = defaultConfig.getString("permissions.examine-inventory-interaction.online");
        String permissionExamineInventoryInteractionOffline = defaultConfig.getString("permissions.examine-inventory-interaction.offline");
        String permissionExamineViewInventory = defaultConfig.getString("permissions.examine-view-inventory.online");
        String permissionExamineViewInventoryOffline = defaultConfig.getString("permissions.examine-view-inventory.offline");
        String commandExamine = defaultConfig.getString("commands.examine");

        return new ExamineConfiguration(permissionExamine, permissionExamineInventoryInteraction, permissionExamineInventoryInteractionOffline, permissionExamineViewInventory, permissionExamineViewInventoryOffline, commandExamine );
    }
}

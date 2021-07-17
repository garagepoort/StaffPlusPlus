package net.shortninja.staffplus.core.domain.staff.examine.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;

@IocBean
public class ExamineModuleLoader extends AbstractConfigLoader<ExamineConfiguration> {

    @Override
    protected ExamineConfiguration load() {
        String permissionExamine = permissionsConfig.getString("examine");
        String permissionExamineInventoryInteraction = permissionsConfig.getString("examine-inventory-interaction.online");
        String permissionExamineInventoryInteractionOffline = permissionsConfig.getString("examine-inventory-interaction.offline");
        String permissionExamineViewInventory = permissionsConfig.getString("examine-view-inventory.online");
        String permissionExamineViewInventoryOffline = permissionsConfig.getString("examine-view-inventory.offline");
        String commandExamine = commandsConfig.getString("examine");

        return new ExamineConfiguration(permissionExamine, permissionExamineInventoryInteraction, permissionExamineInventoryInteractionOffline, permissionExamineViewInventory, permissionExamineViewInventoryOffline, commandExamine );
    }
}

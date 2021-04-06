package net.shortninja.staffplus.core.domain.staff.examine.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class ExamineModuleLoader extends AbstractConfigLoader<ExamineConfiguration> {

    @Override
    protected ExamineConfiguration load() {
        String permissionExamine = permissionsConfig.getString("permissions.examine");
        String permissionExamineInventoryInteraction = permissionsConfig.getString("permissions.examine-inventory-interaction.online");
        String permissionExamineInventoryInteractionOffline = permissionsConfig.getString("permissions.examine-inventory-interaction.offline");
        String permissionExamineViewInventory = permissionsConfig.getString("permissions.examine-view-inventory.online");
        String permissionExamineViewInventoryOffline = permissionsConfig.getString("permissions.examine-view-inventory.offline");
        String commandExamine = commandsConfig.getString("commands.examine");

        return new ExamineConfiguration(permissionExamine, permissionExamineInventoryInteraction, permissionExamineInventoryInteractionOffline, permissionExamineViewInventory, permissionExamineViewInventoryOffline, commandExamine );
    }
}

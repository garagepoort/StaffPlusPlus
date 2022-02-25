package net.shortninja.staffplus.core.domain.staff.examine.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

import java.util.List;

@IocBean
public class ExamineConfiguration {

    @ConfigProperty("permissions:examine-inventory-interaction.online")
    private String permissionExamineInventoryInteraction;
    @ConfigProperty("permissions:examine-inventory-interaction.offline")
    private String permissionExamineInventoryInteractionOffline;
    @ConfigProperty("permissions:examine-view-inventory.online")
    private String permissionExamineViewInventory;
    @ConfigProperty("permissions:examine-view-inventory.offline")
    private String permissionExamineViewInventoryOffline;
    @ConfigProperty("commands:examine")
    private List<String> commandExamine;

    public String getPermissionExamineInventoryInteraction() {
        return permissionExamineInventoryInteraction;
    }

    public String getPermissionExamineViewInventory() {
        return permissionExamineViewInventory;
    }

    public String getPermissionExamineInventoryInteractionOffline() {
        return permissionExamineInventoryInteractionOffline;
    }

    public String getPermissionExamineViewInventoryOffline() {
        return permissionExamineViewInventoryOffline;
    }

    public String getCommandExamine() {
        return commandExamine.get(0);
    }
}

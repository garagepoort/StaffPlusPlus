package net.shortninja.staffplus.core.domain.staff.examine.config;

public class ExamineConfiguration {

    private final String permissionExamine;
    private final String permissionExamineInventoryInteraction;
    private final String permissionExamineInventoryInteractionOffline;
    private final String permissionExamineViewInventory;
    private final String permissionExamineViewInventoryOffline;
    private final String commandExamine;

    public ExamineConfiguration(String permissionExamine, String permissionExamineInventoryInteraction,
                                String permissionExamineInventoryInteractionOffline,
                                String permissionExamineViewInventory,
                                String permissionExamineViewInventoryOffline, String commandExamine) {
        this.permissionExamine = permissionExamine;
        this.permissionExamineInventoryInteraction = permissionExamineInventoryInteraction;
        this.permissionExamineInventoryInteractionOffline = permissionExamineInventoryInteractionOffline;
        this.permissionExamineViewInventory = permissionExamineViewInventory;
        this.permissionExamineViewInventoryOffline = permissionExamineViewInventoryOffline;
        this.commandExamine = commandExamine;
    }

    public String getPermissionExamine() {
        return permissionExamine;
    }

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
        return commandExamine;
    }
}

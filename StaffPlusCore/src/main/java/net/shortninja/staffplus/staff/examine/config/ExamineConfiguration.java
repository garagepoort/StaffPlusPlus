package net.shortninja.staffplus.staff.examine.config;

public class ExamineConfiguration {

    public String permissionExamine;
    public String permissionExamineInventoryInteraction;
    public String permissionExamineViewInventory;
    public String commandExamine;

    public ExamineConfiguration(String permissionExamine, String permissionExamineInventoryInteraction, String permissionExamineViewInventory, String commandExamine) {
        this.permissionExamine = permissionExamine;
        this.permissionExamineInventoryInteraction = permissionExamineInventoryInteraction;
        this.permissionExamineViewInventory = permissionExamineViewInventory;
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

    public String getCommandExamine() {
        return commandExamine;
    }
}

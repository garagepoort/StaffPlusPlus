package net.shortninja.staffplus.staff.warn.warnings.config;

public class ManageWarningsConfiguration {

    private String commandManageWarningsGui;
    private String commandManageAppealedWarningsGui;
    private String permissionView;
    private String permissionDelete;


    public ManageWarningsConfiguration(String commandManageWarningsGui, String commandManageAppealedWarningsGui, String permissionView, String permissionDelete) {
        this.commandManageWarningsGui = commandManageWarningsGui;
        this.commandManageAppealedWarningsGui = commandManageAppealedWarningsGui;
        this.permissionView = permissionView;
        this.permissionDelete = permissionDelete;
    }

    public String getCommandManageWarningsGui() {
        return commandManageWarningsGui;
    }

    public String getCommandManageAppealedWarningsGui() {
        return commandManageAppealedWarningsGui;
    }

    public String getPermissionView() {
        return permissionView;
    }

    public String getPermissionDelete() {
        return permissionDelete;
    }
}

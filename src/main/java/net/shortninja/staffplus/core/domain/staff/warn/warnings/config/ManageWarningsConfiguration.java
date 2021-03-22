package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

public class ManageWarningsConfiguration {

    private String commandManageWarningsGui;
    private String commandManageAppealedWarningsGui;
    private String permissionView;
    private String permissionDelete;
    private String permissionExpire;


    public ManageWarningsConfiguration(String commandManageWarningsGui, String commandManageAppealedWarningsGui, String permissionView, String permissionDelete, String permissionExpire) {
        this.commandManageWarningsGui = commandManageWarningsGui;
        this.commandManageAppealedWarningsGui = commandManageAppealedWarningsGui;
        this.permissionView = permissionView;
        this.permissionDelete = permissionDelete;
        this.permissionExpire = permissionExpire;
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

    public String getPermissionExpire() {
        return permissionExpire;
    }
}

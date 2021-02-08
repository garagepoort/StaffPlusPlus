package net.shortninja.staffplus.staff.warn.config;

public class ManageWarningsConfiguration {

    private String commandManageWarningsGui;
    private String permissionView;
    private String permissionDelete;
    private String permissionApproveAppeal;


    public ManageWarningsConfiguration(String commandManageWarningsGui, String permissionView, String permissionDelete) {
        this.commandManageWarningsGui = commandManageWarningsGui;
        this.permissionView = permissionView;
        this.permissionDelete = permissionDelete;
    }

    public String getCommandManageWarningsGui() {
        return commandManageWarningsGui;
    }



    public String getPermissionView() {
        return permissionView;
    }

    public String getPermissionDelete() {
        return permissionDelete;
    }
}

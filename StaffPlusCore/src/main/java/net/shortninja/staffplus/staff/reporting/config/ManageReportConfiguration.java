package net.shortninja.staffplus.staff.reporting.config;

public class ManageReportConfiguration {

    private String commandManageReportsGui;
    private String permissionView;
    private String permissionDelete;
    private String permissionAccept;
    private String permissionResolve;
    private String permissionReject;


    public ManageReportConfiguration(String commandManageReportsGui, String permissionView, String permissionDelete, String permissionAccept, String permissionResolve, String permissionReject) {
        this.commandManageReportsGui = commandManageReportsGui;
        this.permissionView = permissionView;
        this.permissionDelete = permissionDelete;
        this.permissionAccept = permissionAccept;
        this.permissionResolve = permissionResolve;
        this.permissionReject = permissionReject;
    }

    public String getCommandManageReportsGui() {
        return commandManageReportsGui;
    }

    public String getPermissionView() {
        return permissionView;
    }

    public String getPermissionDelete() {
        return permissionDelete;
    }

    public String getPermissionAccept() {
        return permissionAccept;
    }

    public String getPermissionResolve() {
        return permissionResolve;
    }

    public String getPermissionReject() {
        return permissionReject;
    }
}

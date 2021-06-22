package net.shortninja.staffplus.core.domain.staff.alerts.config;

import net.shortninja.staffplus.core.domain.staff.alerts.xray.XrayBlockConfig;

import java.util.List;

public class XrayConfiguration {
    private final List<XrayBlockConfig> alertsXrayBlocks;
    private final String permissionXray;
    private final String permissionXrayBypass;

    public XrayConfiguration(List<XrayBlockConfig> alertsXrayBlocks, String permissionXray, String permissionXrayBypass) {
        this.alertsXrayBlocks = alertsXrayBlocks;
        this.permissionXray = permissionXray;
        this.permissionXrayBypass = permissionXrayBypass;
    }

    public List<XrayBlockConfig> getAlertsXrayBlocks() {
        return alertsXrayBlocks;
    }

    public String getPermissionXray() {
        return permissionXray;
    }

    public String getPermissionXrayBypass() {
        return permissionXrayBypass;
    }
}

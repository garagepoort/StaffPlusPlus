package net.shortninja.staffplus.core.domain.staff.alerts.config;

import net.shortninja.staffplus.core.domain.staff.alerts.xray.XrayBlockConfig;

import java.util.List;

public class XrayConfiguration {
    private final List<XrayBlockConfig> alertsXrayBlocks;
    private final String permissionXray;

    public XrayConfiguration(List<XrayBlockConfig> alertsXrayBlocks, String permissionXray) {
        this.alertsXrayBlocks = alertsXrayBlocks;
        this.permissionXray = permissionXray;
    }

    public List<XrayBlockConfig> getAlertsXrayBlocks() {
        return alertsXrayBlocks;
    }

    public String getPermissionXray() {
        return permissionXray;
    }
}

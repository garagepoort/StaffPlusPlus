package net.shortninja.staffplus.core.domain.staff.alerts.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.XrayBlockConfig;
import org.bukkit.Material;

import java.util.List;
import java.util.Optional;

@IocBean
public class XrayConfiguration {
    @ConfigProperty("alerts-module.xray-alerts.blocks")
    @ConfigTransformer(XrayBlockConfigTransformer.class)
    public List<XrayBlockConfig> alertsXrayBlocks;
    @ConfigProperty("permissions:xray")
    public String permissionXray;
    @ConfigProperty("permissions:xray-bypass")
    public String permissionXrayBypass;

    public Optional<XrayBlockConfig> getBlockConfig(Material type) {
        return alertsXrayBlocks.stream()
            .filter(b -> b.getMaterial() == type)
            .findFirst();
    }
}

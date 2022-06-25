package net.shortninja.staffplus.core.domain.staff.kick.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigurationLoader;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@IocBean
public class KickModuleLoader extends AbstractConfigLoader<KickConfiguration> {

    public KickModuleLoader(ConfigurationLoader configurationLoader) {
        super(configurationLoader);
    }

    @Override
    protected KickConfiguration load() {
        boolean kickEnabled = defaultConfig.getBoolean("kick-module.enabled");
        String permissionKickPlayer = permissionsConfig.getString("kick");
        String permissionKickByPass = permissionsConfig.getString("kick-bypass");

        boolean fixedReason = defaultConfig.getBoolean("kick-module.fixed-reason", false);
        return new KickConfiguration(kickEnabled, permissionKickPlayer, permissionKickByPass, fixedReason, getKickReasons(defaultConfig));
    }

    private List<KickReasonConfiguration> getKickReasons(ConfigurationSection config) {
        List<LinkedHashMap<String, String>> list = (List<LinkedHashMap<String, String>>) config.getList("kick-module.reasons", new ArrayList<>());

        return Objects.requireNonNull(list).stream().map(map -> {
            String reason = map.get("reason");
            String lore = map.get("info");
            Material material = map.containsKey("material") ? Material.valueOf(map.get("material")) : Material.PAPER;
            return new KickReasonConfiguration(reason, material, lore);
        }).collect(Collectors.toList());
    }
}

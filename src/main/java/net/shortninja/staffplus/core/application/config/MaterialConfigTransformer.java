package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import org.bukkit.Material;

public class MaterialConfigTransformer implements IConfigTransformer<Material, String> {
    @Override
    public Material mapConfig(String configValue) {
        return Material.valueOf(configValue);
    }
}

package net.shortninja.staffplus.core.punishments.kick.config;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToEnum;
import org.bukkit.Material;

public class KickReasonConfiguration {

    @ConfigProperty("reason")
    public String reason;
    @ConfigProperty("material")
    @ConfigTransformer(ToEnum.class)
    public Material material = Material.PAPER;
    @ConfigProperty("info")
    public String lore;

    public String getReason() {
        return reason;
    }

    public Material getMaterial() {
        return material;
    }

    public String getLore() {
        return lore;
    }
}

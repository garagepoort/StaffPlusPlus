package net.shortninja.staffplus.core.domain.staff.kick.config;

import org.bukkit.Material;

public class KickReasonConfiguration {

    private String reason;
    private Material material;
    private String lore;

    public KickReasonConfiguration(String reason, Material material, String lore) {
        this.reason = reason;
        this.material = material;
        this.lore = lore;
    }

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

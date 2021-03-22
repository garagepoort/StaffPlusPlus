package net.shortninja.staffplus.core.domain.staff.reporting.config;

import org.bukkit.Material;

public class ReportTypeConfiguration {

    private String type;
    private Material material;
    private String lore;

    public ReportTypeConfiguration(String type, Material material, String lore) {
        this.type = type;
        this.material = material;
        this.lore = lore;
    }

    public String getType() {
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public String getLore() {
        return lore;
    }
}

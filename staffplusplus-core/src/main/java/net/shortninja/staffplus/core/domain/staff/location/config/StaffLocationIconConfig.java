package net.shortninja.staffplus.core.domain.staff.location.config;

import be.garagepoort.mcioc.configuration.ConfigProperty;

public class StaffLocationIconConfig {

    @ConfigProperty("icon")
    private String material;
    @ConfigProperty("text")
    private String iconText;

    // Empty constructor needed for Tubing initialization
    public StaffLocationIconConfig() {
    }

    public StaffLocationIconConfig(String material, String iconText) {
        this.material = material;
        this.iconText = iconText;
    }

    public String getMaterial() {
        return material;
    }

    public String getIconText() {
        return iconText;
    }
}

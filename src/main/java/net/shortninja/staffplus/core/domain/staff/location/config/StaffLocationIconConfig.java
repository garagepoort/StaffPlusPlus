package net.shortninja.staffplus.core.domain.staff.location.config;

public class StaffLocationIconConfig {

    private final String material;
    private final String iconText;

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

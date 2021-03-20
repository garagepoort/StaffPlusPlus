package net.shortninja.staffplus.domain.staff.mode.config.gui;

import java.util.Map;

public class GuiConfiguration {

    private String permission;
    private int weight;
    private Map<String, Integer> itemSlots;

    public GuiConfiguration(String permission, int weight, Map<String, Integer> itemSlots) {
        this.permission = permission;
        this.weight = weight;
        this.itemSlots = itemSlots;
    }

    public String getPermission() {
        return permission;
    }

    public Map<String, Integer> getItemSlots() {
        return itemSlots;
    }

    public int getWeight() {
        return weight;
    }
}

package net.shortninja.staffplus.player.attribute.mode.item;

import org.bukkit.inventory.ItemStack;

public class ModuleConfiguration {
    private ModuleType moduleType;
    private String identifier;
    private int slot;
    private ItemStack item;
    private String action;

    private String enchantment;
    private int level;

    public ModuleConfiguration(String identifier, ModuleType moduleType, int slot, ItemStack item, String action) {
        this.identifier = identifier;
        this.moduleType = moduleType;
        this.slot = slot;
        this.item = item;
        this.action = action;

        this.enchantment = "";
        this.level = 0;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ModuleType getType() {
        return moduleType;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getAction() {
        return action;
    }

    public String getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    public enum ModuleType {
        COMMAND_STATIC, COMMAND_DYNAMIC, COMMAND_CONSOLE, ITEM;
    }
}
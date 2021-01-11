package net.shortninja.staffplus.staff.mode.item;

import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;
import org.bukkit.inventory.ItemStack;

public class CustomModuleConfiguration extends ModeItemConfiguration {
    private ModuleType moduleType;
    private String action;

    private String enchantment;
    private int level;

    public CustomModuleConfiguration(boolean enabled, String identifier, ModuleType moduleType, int slot, ItemStack item, String action) {
        super(identifier);
        setEnabled(enabled);
        setSlot(slot);
        setItem(item);
        this.moduleType = moduleType;
        this.action = action;
        this.enchantment = "";
        this.level = 0;
    }

    public ModuleType getType() {
        return moduleType;
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
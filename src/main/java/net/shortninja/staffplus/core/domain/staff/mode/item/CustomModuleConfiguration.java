package net.shortninja.staffplus.core.domain.staff.mode.item;

import net.shortninja.staffplus.core.domain.confirmation.ConfirmationConfig;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class CustomModuleConfiguration extends ModeItemConfiguration {
    private final ModuleType moduleType;
    private final List<String> actions;
    private final String enchantment;
    private final int level;
    private final ConfirmationConfig confirmationConfig;
    private final boolean requireInput;
    private final String inputPrompt;

    public CustomModuleConfiguration(boolean enabled, String identifier, ModuleType moduleType, ItemStack item, List<String> actions, ConfirmationConfig confirmationConfig, boolean requireInput, String inputPrompt) {
        super(identifier);
        this.confirmationConfig = confirmationConfig;
        this.requireInput = requireInput;
        this.inputPrompt = inputPrompt;
        this.moduleType = moduleType;
        this.actions = actions;
        this.enchantment = "";
        this.level = 0;
        setEnabled(enabled);
        setItem(item);
    }

    public ModuleType getType() {
        return moduleType;
    }

    public List<String> getActions() {
        return actions;
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

    public Optional<ConfirmationConfig> getConfirmationConfig() {
        return Optional.ofNullable(confirmationConfig);
    }

    public boolean isRequireInput() {
        return requireInput;
    }

    public String getInputPrompt() {
        return inputPrompt;
    }
}
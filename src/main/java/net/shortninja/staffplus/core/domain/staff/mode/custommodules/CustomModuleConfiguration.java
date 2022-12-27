package net.shortninja.staffplus.core.domain.staff.mode.custommodules;

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
    private final String enabledOnState;
    private final String enableState;
    private String disableState;

    public CustomModuleConfiguration(boolean enabled,
                                     boolean movable,
                                     String identifier,
                                     ModuleType moduleType,
                                     ItemStack item,
                                     List<String> actions,
                                     ConfirmationConfig confirmationConfig,
                                     boolean requireInput,
                                     String inputPrompt,
                                     String enabledOnState,
                                     String enableState,
                                     String disableState) {
        super(identifier);
        this.confirmationConfig = confirmationConfig;
        this.requireInput = requireInput;
        this.inputPrompt = inputPrompt;
        this.moduleType = moduleType;
        this.actions = actions;
        this.enabledOnState = enabledOnState;
        this.enableState = enableState;
        this.disableState = disableState;
        this.enchantment = "";
        this.level = 0;
        setEnabled(enabled);
        setMovable(movable);
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

    public Optional<String> getEnabledOnState() {
        return Optional.ofNullable(enabledOnState);
    }

    public Optional<String> getEnableState() {
        return Optional.ofNullable(enableState);
    }

    public Optional<String> getDisableState() {
        return Optional.ofNullable(disableState);
    }
}
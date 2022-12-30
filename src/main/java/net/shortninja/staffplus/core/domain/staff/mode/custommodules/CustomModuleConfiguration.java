package net.shortninja.staffplus.core.domain.staff.mode.custommodules;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToEnum;
import net.shortninja.staffplus.core.domain.confirmation.ConfirmationConfig;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.item.ConfirmationType;

import java.util.List;
import java.util.Optional;

public class CustomModuleConfiguration extends ModeItemConfiguration {
    @ConfigProperty("type")
    @ConfigTransformer(ToEnum.class)
    private ModuleType moduleType;
    @ConfigProperty("commands")
    private List<String> actions;

    @ConfigProperty("confirmation")
    @ConfigTransformer(ToEnum.class)
    private ConfirmationType confirmationType;
    @ConfigProperty("confirmation-message")
    private String confirmationMessage;

    @ConfigProperty("require-input")
    private boolean requireInput;
    @ConfigProperty("input-prompt")
    private String inputPrompt;

    @ConfigProperty("enabled-on-state")
    private String enabledOnState;
    @ConfigProperty("enable-state")
    private String enableState;
    @ConfigProperty("disable-state")
    private String disableState;

    public ModuleType getType() {
        return moduleType;
    }

    public List<String> getActions() {
        return actions;
    }

    public enum ModuleType {
        COMMAND_STATIC, COMMAND_DYNAMIC, COMMAND_CONSOLE, ITEM;
    }

    public Optional<ConfirmationConfig> getConfirmationConfig() {
        return Optional.ofNullable(confirmationType != null ? new ConfirmationConfig(confirmationType, confirmationMessage) : null);
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
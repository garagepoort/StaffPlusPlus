package net.shortninja.staffplus.core.domain.staff.mode.config;

import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.staff.mode.config.gui.GuiConfiguration;
import net.shortninja.staffplusplus.vanish.VanishType;

import java.util.List;

public class GeneralModeConfiguration {

    private VanishType modeVanish;
    private boolean modeItemPickup;
    private boolean modeItemDrop;
    private boolean modeDamage;
    private boolean modeHungerLoss;
    private final List<ConfiguredAction> modeEnableCommands;
    private final List<ConfiguredAction> modeDisableCommands;
    private boolean worldChange;
    private boolean modeBlockManipulation;
    private boolean modeInventoryInteraction;
    private boolean modeSilentChestInteraction;
    private boolean modeInvincible;
    private boolean modeFlight;
    private boolean modeCreative;
    private boolean modeOriginalLocation;
    private boolean modeEnableOnLogin;
    private boolean modeDisableOnLogout;

    private final List<GuiConfiguration> guiConfigurations;

    public GeneralModeConfiguration(VanishType modeVanish,
                                    boolean modeItemPickup,
                                    boolean modeItemDrop, boolean modeDamage,
                                    boolean modeHungerLoss,
                                    List<ConfiguredAction> modeEnableCommands,
                                    List<ConfiguredAction> modeDisableCommands,
                                    boolean worldChange,
                                    boolean modeBlockManipulation,
                                    boolean modeInventoryInteraction,
                                    boolean modeSilentChestInteraction,
                                    boolean modeInvincible,
                                    boolean modeFlight,
                                    boolean modeCreative,
                                    boolean modeOriginalLocation,
                                    boolean modeEnableOnLogin,
                                    boolean modeDisableOnLogout,
                                    List<GuiConfiguration> guiConfigurations) {
        this.modeVanish = modeVanish;
        this.modeItemPickup = modeItemPickup;
        this.modeItemDrop = modeItemDrop;
        this.modeDamage = modeDamage;
        this.modeHungerLoss = modeHungerLoss;
        this.modeEnableCommands = modeEnableCommands;
        this.modeDisableCommands = modeDisableCommands;
        this.worldChange = worldChange;
        this.modeBlockManipulation = modeBlockManipulation;
        this.modeInventoryInteraction = modeInventoryInteraction;
        this.modeSilentChestInteraction = modeSilentChestInteraction;
        this.modeInvincible = modeInvincible;
        this.modeFlight = modeFlight;
        this.modeCreative = modeCreative;
        this.modeOriginalLocation = modeOriginalLocation;
        this.modeEnableOnLogin = modeEnableOnLogin;
        this.modeDisableOnLogout = modeDisableOnLogout;
        this.guiConfigurations = guiConfigurations;
    }

    public boolean isModeItemPickup() {
        return modeItemPickup;
    }

    public boolean isModeItemDrop() {
        return modeItemDrop;
    }

    public List<GuiConfiguration> getStaffGuiConfigurations() {
        return guiConfigurations;
    }

    public VanishType getModeVanish() {
        return modeVanish;
    }

    public boolean isModeDamage() {
        return modeDamage;
    }

    public boolean isModeHungerLoss() {
        return modeHungerLoss;
    }

    public List<ConfiguredAction> getModeEnableCommands() {
        return modeEnableCommands;
    }

    public List<ConfiguredAction> getModeDisableCommands() {
        return modeDisableCommands;
    }

    public boolean isWorldChange() {
        return worldChange;
    }

    public boolean isModeBlockManipulation() {
        return modeBlockManipulation;
    }

    public boolean isModeInventoryInteraction() {
        return modeInventoryInteraction;
    }

    public boolean isModeSilentChestInteraction() {
        return modeSilentChestInteraction;
    }

    public boolean isModeInvincible() {
        return modeInvincible;
    }

    public boolean isModeFlight() {
        return modeFlight;
    }

    public boolean isModeCreative() {
        return modeCreative;
    }

    public boolean isModeOriginalLocation() {
        return modeOriginalLocation;
    }

    public boolean isModeEnableOnLogin() {
        return modeEnableOnLogin;
    }

    public boolean isModeDisableOnLogout() {
        return modeDisableOnLogout;
    }

}

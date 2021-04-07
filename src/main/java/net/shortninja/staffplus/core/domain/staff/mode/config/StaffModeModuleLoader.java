package net.shortninja.staffplus.core.domain.staff.mode.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.domain.actions.ActionConfigLoader;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.staff.mode.config.gui.GuiConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.gui.StaffModeGuiConfigurationLoader;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@IocBean
public class StaffModeModuleLoader extends AbstractConfigLoader<GeneralModeConfiguration> {

    private final StaffModeGuiConfigurationLoader staffModeGuiConfigurationLoader;


    public StaffModeModuleLoader(StaffModeGuiConfigurationLoader staffModeGuiConfigurationLoader) {
        this.staffModeGuiConfigurationLoader = staffModeGuiConfigurationLoader;
    }

    @Override
    protected GeneralModeConfiguration load() {
        VanishType modeVanish = stringToVanishType(defaultConfig.getString("staff-mode.vanish-type"));
        boolean modeItemDrop = defaultConfig.getBoolean("staff-mode.item-drop");
        boolean modeItemPickup = defaultConfig.getBoolean("staff-mode.item-pickup");
        boolean modeDamage = defaultConfig.getBoolean("staff-mode.damage");
        boolean modeHungerLoss = defaultConfig.getBoolean("staff-mode.hunger-loss");
        List<ConfiguredAction> modeEnableCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) defaultConfig.getList("staff-mode.enable-commands", new ArrayList<>()));
        List<ConfiguredAction> modeDisableCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) defaultConfig.getList("staff-mode.disable-commands", new ArrayList<>()));
        boolean worldChange = defaultConfig.getBoolean("staff-mode.disable-on-world-change");
        boolean modeBlockManipulation = defaultConfig.getBoolean("staff-mode.block-manipulation");
        boolean modeInventoryInteraction = defaultConfig.getBoolean("staff-mode.inventory-interaction");
        boolean modeSilentChestInteraction = defaultConfig.getBoolean("staff-mode.silent-chest-interaction");
        boolean modeInvincible = defaultConfig.getBoolean("staff-mode.invincible");
        boolean modeFlight = defaultConfig.getBoolean("staff-mode.flight");
        boolean modeCreative = defaultConfig.getBoolean("staff-mode.creative");
        boolean modeOriginalLocation = defaultConfig.getBoolean("staff-mode.original-location");
        boolean modeEnableOnLogin = defaultConfig.getBoolean("staff-mode.enable-on-login");
        boolean modeDisableOnLogout = defaultConfig.getBoolean("staff-mode.disable-on-logout");

        List<GuiConfiguration> guiConfigurations = staffModeGuiConfigurationLoader.loadConfig();

        return new GeneralModeConfiguration(modeVanish,
            modeItemPickup, modeItemDrop, modeDamage,
            modeHungerLoss,
            modeEnableCommands,
            modeDisableCommands,
            worldChange,
            modeBlockManipulation,
            modeInventoryInteraction,
            modeSilentChestInteraction,
            modeInvincible,
            modeFlight,
            modeCreative,
            modeOriginalLocation,
            modeEnableOnLogin,
            modeDisableOnLogout,
            guiConfigurations);
    }

    private VanishType stringToVanishType(String string) {
        VanishType vanishType = VanishType.NONE;
        boolean isValid = JavaUtils.isValidEnum(VanishType.class, string);

        if (!isValid) {
            Bukkit.getLogger().severe("Invalid vanish type '" + string + "'!");
        } else vanishType = VanishType.valueOf(string);

        return vanishType;
    }
}

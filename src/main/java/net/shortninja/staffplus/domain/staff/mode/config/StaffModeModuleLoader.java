package net.shortninja.staffplus.domain.staff.mode.config;

import net.shortninja.staffplus.domain.actions.ActionConfigLoader;
import net.shortninja.staffplus.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplusplus.vanish.VanishType;
import net.shortninja.staffplus.common.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StaffModeModuleLoader extends ConfigLoader<GeneralModeConfiguration> {

    @Override
    protected GeneralModeConfiguration load(FileConfiguration config) {
        VanishType modeVanish = stringToVanishType(config.getString("staff-mode.vanish-type"));
        boolean modeItemDrop = config.getBoolean("staff-mode.item-drop");
        boolean modeItemPickup = config.getBoolean("staff-mode.item-pickup");
        boolean modeDamage = config.getBoolean("staff-mode.damage");
        boolean modeHungerLoss = config.getBoolean("staff-mode.hunger-loss");
        List<ConfiguredAction> modeEnableCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) config.getList("staff-mode.enable-commands", new ArrayList<>()));
        List<ConfiguredAction> modeDisableCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) config.getList("staff-mode.disable-commands", new ArrayList<>()));
        boolean worldChange = config.getBoolean("staff-mode.disable-on-world-change");
        boolean modeBlockManipulation = config.getBoolean("staff-mode.block-manipulation");
        boolean modeInventoryInteraction = config.getBoolean("staff-mode.inventory-interaction");
        boolean modeSilentChestInteraction = config.getBoolean("staff-mode.silent-chest-interaction");
        boolean modeInvincible = config.getBoolean("staff-mode.invincible");
        boolean modeFlight = config.getBoolean("staff-mode.flight");
        boolean modeCreative = config.getBoolean("staff-mode.creative");
        boolean modeOriginalLocation = config.getBoolean("staff-mode.original-location");
        boolean modeEnableOnLogin = config.getBoolean("staff-mode.enable-on-login");
        boolean modeDisableOnLogout = config.getBoolean("staff-mode.disable-on-logout");

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
            modeDisableOnLogout);
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

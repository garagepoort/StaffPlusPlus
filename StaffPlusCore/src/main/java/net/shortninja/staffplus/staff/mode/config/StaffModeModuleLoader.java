package net.shortninja.staffplus.staff.mode.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplusplus.vanish.VanishType;
import net.shortninja.staffplus.common.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class StaffModeModuleLoader extends ConfigLoader<GeneralModeConfiguration> {

    @Override
    protected GeneralModeConfiguration load(FileConfiguration config) {
        VanishType modeVanish = stringToVanishType(config.getString("staff-mode.vanish-type"));
        boolean modeItemDrop = config.getBoolean("staff-mode.item-drop");
        boolean modeItemPickup = config.getBoolean("staff-mode.item-pickup");
        boolean modeDamage = config.getBoolean("staff-mode.damage");
        boolean modeHungerLoss = config.getBoolean("staff-mode.hunger-loss");
        List<String> modeEnableCommands = JavaUtils.stringToList(config.getString("staff-mode.enable-commands"));
        List<String> modeDisableCommands = JavaUtils.stringToList(config.getString("staff-mode.disable-commands"));
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

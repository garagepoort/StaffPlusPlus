package net.shortninja.staffplus.core.domain.staff.mode.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.domain.actions.ActionConfigLoader;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.staff.mode.config.gui.GuiConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.gui.StaffModeGuiConfigurationLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.compass.CompassModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.compass.CompassModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.counter.CounterModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.counter.CounterModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.cps.CpsModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.cps.CpsModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.follow.FollowModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.follow.FollowModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui.GuiModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.randomteleport.RandomTeleportModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.randomteleport.RandomTeleportModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeItemLoader;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@IocBean
public class StaffModeModuleLoader extends AbstractConfigLoader<GeneralModeConfiguration> {

    private final StaffModeGuiConfigurationLoader staffModeGuiConfigurationLoader;
    private final CompassModeItemLoader compassModeItemLoader;
    private final CounterModeItemLoader counterModeItemLoader;
    private final CpsModeItemLoader cpsModeItemLoader;
    private final ExamineModeItemLoader examineModeItemLoader;
    private final FollowModeItemLoader followModeItemLoader;
    private final FreezeModeItemLoader freezeModeItemLoader;
    private final GuiModeItemLoader guiModeItemLoader;
    private final RandomTeleportModeItemLoader randomTeleportModeItemLoader;
    private final VanishModeItemLoader vanishModeItemLoader;

    public StaffModeModuleLoader(StaffModeGuiConfigurationLoader staffModeGuiConfigurationLoader,
                                 CompassModeItemLoader compassModeItemLoader,
                                 CounterModeItemLoader counterModeItemLoader,
                                 CpsModeItemLoader cpsModeItemLoader,
                                 ExamineModeItemLoader examineModeItemLoader,
                                 FollowModeItemLoader followModeItemLoader,
                                 FreezeModeItemLoader freezeModeItemLoader,
                                 GuiModeItemLoader guiModeItemLoader,
                                 RandomTeleportModeItemLoader randomTeleportModeItemLoader,
                                 VanishModeItemLoader vanishModeItemLoader) {
        this.staffModeGuiConfigurationLoader = staffModeGuiConfigurationLoader;
        this.compassModeItemLoader = compassModeItemLoader;
        this.counterModeItemLoader = counterModeItemLoader;
        this.cpsModeItemLoader = cpsModeItemLoader;
        this.examineModeItemLoader = examineModeItemLoader;
        this.followModeItemLoader = followModeItemLoader;
        this.freezeModeItemLoader = freezeModeItemLoader;
        this.guiModeItemLoader = guiModeItemLoader;
        this.randomTeleportModeItemLoader = randomTeleportModeItemLoader;
        this.vanishModeItemLoader = vanishModeItemLoader;
    }

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

        List<GuiConfiguration> guiConfigurations = staffModeGuiConfigurationLoader.loadConfig();
        CompassModeConfiguration compassModeConfiguration = compassModeItemLoader.loadConfig();
        CounterModeConfiguration counterModeConfiguration = counterModeItemLoader.loadConfig();
        CpsModeConfiguration cpsModeConfiguration = cpsModeItemLoader.loadConfig();
        ExamineModeConfiguration examineModeConfiguration = examineModeItemLoader.loadConfig();
        FollowModeConfiguration followModeConfiguration = followModeItemLoader.loadConfig();
        FreezeModeConfiguration freezeModeConfiguration = freezeModeItemLoader.loadConfig();
        GuiModeConfiguration guiModeConfiguration = guiModeItemLoader.loadConfig();
        RandomTeleportModeConfiguration randomTeleportModeConfiguration = randomTeleportModeItemLoader.loadConfig();
        VanishModeConfiguration vanishModeConfiguration = vanishModeItemLoader.loadConfig();

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
            guiConfigurations,
            compassModeConfiguration,
            counterModeConfiguration,
            cpsModeConfiguration,
            examineModeConfiguration,
            followModeConfiguration,
            freezeModeConfiguration,
            guiModeConfiguration,
            randomTeleportModeConfiguration,
            vanishModeConfiguration);
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

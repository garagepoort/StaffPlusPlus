package net.shortninja.staffplus.staff.mode.config;

import net.shortninja.staffplus.staff.mode.config.gui.GuiConfiguration;
import net.shortninja.staffplus.staff.mode.config.gui.StaffModeGuiConfigurationLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.compass.CompassModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.compass.CompassModeItemLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.counter.CounterModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.counter.CounterModeItemLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.cps.CpsModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.cps.CpsModeItemLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeItemLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.follow.FollowModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.follow.FollowModeItemLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.freeze.FreezeModeItemLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.gui.GuiModeItemLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.randomteleport.RandomTeleportModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.randomteleport.RandomTeleportModeItemLoader;
import net.shortninja.staffplus.staff.mode.config.modeitems.vanish.VanishModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.vanish.VanishModeItemLoader;
import net.shortninja.staffplus.unordered.VanishType;

import java.util.ArrayList;
import java.util.List;

public class GeneralModeConfiguration {

    private VanishType modeVanish;
    private boolean modeItemChange;
    private boolean modeDamage;
    private boolean modeHungerLoss;
    private List<String> modeEnableCommands;
    private List<String> modeDisableCommands;
    private boolean worldChange;
    private boolean modeBlockManipulation;
    private boolean modeInventoryInteraction;
    private boolean modeSilentChestInteraction;
    private boolean modeInvincible;
    private boolean modeFlight;
    private boolean modeCreative;
    private boolean modeOriginalLocation;
    private boolean modeEnableOnLogin;


    private List<GuiConfiguration> guiConfigurations = new ArrayList<>();

    private final CompassModeConfiguration compassModeConfiguration;
    private final CounterModeConfiguration counterModeConfiguration;
    private final CpsModeConfiguration cpsModeConfiguration;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final FollowModeConfiguration followModeConfiguration;
    private final FreezeModeConfiguration freezeModeConfiguration;
    private final GuiModeConfiguration guiModeConfiguration;
    private final RandomTeleportModeConfiguration randomTeleportModeConfiguration;
    private final VanishModeConfiguration vanishModeConfiguration;

    public GeneralModeConfiguration(VanishType modeVanish,
                                    boolean modeItemChange,
                                    boolean modeDamage,
                                    boolean modeHungerLoss,
                                    List<String> modeEnableCommands,
                                    List<String> modeDisableCommands,
                                    boolean worldChange,
                                    boolean modeBlockManipulation,
                                    boolean modeInventoryInteraction,
                                    boolean modeSilentChestInteraction,
                                    boolean modeInvincible,
                                    boolean modeFlight,
                                    boolean modeCreative,
                                    boolean modeOriginalLocation,
                                    boolean modeEnableOnLogin) {
        this.modeVanish = modeVanish;
        this.modeItemChange = modeItemChange;
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

        this.guiConfigurations = new StaffModeGuiConfigurationLoader().loadConfig();

        this.compassModeConfiguration = new CompassModeItemLoader().loadConfig();
        this.counterModeConfiguration = new CounterModeItemLoader().loadConfig();
        this.vanishModeConfiguration = new VanishModeItemLoader().loadConfig();
        this.randomTeleportModeConfiguration = new RandomTeleportModeItemLoader().loadConfig();
        this.cpsModeConfiguration = new CpsModeItemLoader().loadConfig();
        this.examineModeConfiguration = new ExamineModeItemLoader().loadConfig();
        this.followModeConfiguration = new FollowModeItemLoader().loadConfig();
        this.freezeModeConfiguration = new FreezeModeItemLoader().loadConfig();
        this.guiModeConfiguration = new GuiModeItemLoader().loadConfig();
    }

    public List<GuiConfiguration> getStaffGuiConfigurations() {
        return guiConfigurations;
    }

    public CompassModeConfiguration getCompassModeConfiguration() {
        return compassModeConfiguration;
    }

    public VanishModeConfiguration getVanishModeConfiguration() {
        return vanishModeConfiguration;
    }

    public RandomTeleportModeConfiguration getRandomTeleportModeConfiguration() {
        return randomTeleportModeConfiguration;
    }

    public CounterModeConfiguration getCounterModeConfiguration() {
        return counterModeConfiguration;
    }

    public CpsModeConfiguration getCpsModeConfiguration() {
        return cpsModeConfiguration;
    }

    public ExamineModeConfiguration getExamineModeConfiguration() {
        return examineModeConfiguration;
    }

    public FollowModeConfiguration getFollowModeConfiguration() {
        return followModeConfiguration;
    }

    public FreezeModeConfiguration getFreezeModeConfiguration() {
        return freezeModeConfiguration;
    }

    public GuiModeConfiguration getGuiModeConfiguration() {
        return guiModeConfiguration;
    }

    public VanishType getModeVanish() {
        return modeVanish;
    }

    public boolean isModeItemChange() {
        return modeItemChange;
    }

    public boolean isModeDamage() {
        return modeDamage;
    }

    public boolean isModeHungerLoss() {
        return modeHungerLoss;
    }

    public List<String> getModeEnableCommands() {
        return modeEnableCommands;
    }

    public List<String> getModeDisableCommands() {
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
}

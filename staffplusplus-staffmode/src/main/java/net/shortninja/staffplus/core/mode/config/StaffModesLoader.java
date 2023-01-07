package net.shortninja.staffplus.core.mode.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigurationLoader;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@IocBean
public class StaffModesLoader extends AbstractConfigLoader<Map<String, GeneralModeConfiguration>> {

    public StaffModesLoader(ConfigurationLoader configurationLoader) {
        super(configurationLoader);
    }

    @Override
    protected Map<String, GeneralModeConfiguration> load() {
        Map<String, GeneralModeConfiguration> generalModeConfigurations = new HashMap<>();
        ConfigurationSection modes = staffModeModesConfig.getConfigurationSection("modes");
        for (String key : modes.getKeys(false)) {
            GeneralModeConfiguration modeConfiguration = new GeneralModeConfiguration();
            modeConfiguration.setName(key);
            modeConfiguration.setWeight(modes.getInt(key + ".weight"));
            modeConfiguration.setPermission(modes.getString(key + ".permission"));
            parseMode(modeConfiguration, modes, key);
            generalModeConfigurations.put(key, modeConfiguration);
        }
        return generalModeConfigurations;
    }

    private void parseMode(GeneralModeConfiguration modeConfiguration, ConfigurationSection modes, String key) {
        ConfigurationSection currentModeConfigSection = modes.getConfigurationSection(key);
        if (!key.equalsIgnoreCase("default")) {
            String extendsConfig = currentModeConfigSection.getString("extends", "default");
            parseMode(modeConfiguration, modes, extendsConfig);
        }
        enhanceMode(modeConfiguration, currentModeConfigSection);
    }

    private void enhanceMode(GeneralModeConfiguration modeConfig, ConfigurationSection configurationSection) {
        if (configurationSection.contains("allowed-worlds")) {
            modeConfig.setValidWorlds(configurationSection.getStringList("allowed-worlds"));
        }
        if (configurationSection.contains("vanish-type")) {
            modeConfig.setModeVanish(stringToVanishType(configurationSection.getString("vanish-type")));
        }
        if (configurationSection.contains("vanish-on-enter")) {
            modeConfig.setVanishOnEnter(configurationSection.getBoolean("vanish-on-enter"));
        }
        if (configurationSection.contains("item-drop")) {
            modeConfig.setModeItemDrop(configurationSection.getBoolean("item-drop"));
        }
        if (configurationSection.contains("item-pickup")) {
            modeConfig.setModeItemPickup(configurationSection.getBoolean("item-pickup"));
        }
        if (configurationSection.contains("damage")) {
            modeConfig.setModeDamage(configurationSection.getBoolean("damage"));
        }
        if (configurationSection.contains("hunger-loss")) {
            modeConfig.setModeHungerLoss(configurationSection.getBoolean("hunger-loss"));
        }
        if (configurationSection.contains("enable-commands")) {
            modeConfig.setModeEnableCommands(configurationSection.getObjectList("enable-commands", ConfiguredCommand.class, new ArrayList<>()));
        }
        if (configurationSection.contains("disable-commands")) {
            modeConfig.setModeDisableCommands(configurationSection.getObjectList("disable-commands", ConfiguredCommand.class, new ArrayList<>()));
        }
        if (configurationSection.contains("disable-on-world-change")) {
            modeConfig.setDisableOnWorldChange(configurationSection.getBoolean("disable-on-world-change"));
        }
        if (configurationSection.contains("block-manipulation")) {
            modeConfig.setModeBlockManipulation(configurationSection.getBoolean("block-manipulation"));
        }
        if (configurationSection.contains("inventory-interaction")) {
            modeConfig.setModeInventoryInteraction(configurationSection.getBoolean("inventory-interaction"));
        }
        if (configurationSection.contains("silent-chest-interaction")) {
            modeConfig.setModeSilentChestInteraction(configurationSection.getBoolean("silent-chest-interaction"));
        }
        if (configurationSection.contains("invincible")) {
            modeConfig.setModeInvincible(configurationSection.getBoolean("invincible"));
        }
        if (configurationSection.contains("flight")) {
            modeConfig.setModeFlight(configurationSection.getBoolean("flight"));
        }
        if (configurationSection.contains("creative")) {
            modeConfig.setModeCreative(configurationSection.getBoolean("creative"));
        }
        if (configurationSection.contains("original-location")) {
            modeConfig.setModeOriginalLocation(configurationSection.getBoolean("original-location"));
        }
        if (configurationSection.contains("enable-on-login")) {
            modeConfig.setModeEnableOnLogin(configurationSection.getBoolean("enable-on-login"));
        }
        if (configurationSection.contains("night-vision")) {
            modeConfig.setNightVision(configurationSection.getBoolean("night-vision"));
        }
        if (configurationSection.contains("disable-on-logout")) {
            modeConfig.setModeDisableOnLogout(configurationSection.getBoolean("disable-on-logout"));
        }
        if (configurationSection.contains("gui")) {
            modeConfig.setItemSlots(getItemSlots(configurationSection.getList("gui", new ArrayList<>())));
        }
        if (configurationSection.contains("initial-item-states")) {
            modeConfig.setInitialItemStates(new HashSet<>(configurationSection.getStringList("initial-item-states")));
        }
    }

    private VanishType stringToVanishType(String string) {
        VanishType vanishType = VanishType.NONE;
        boolean isValid = JavaUtils.isValidEnum(VanishType.class, string);

        if (!isValid) {
            Bukkit.getLogger().severe("Invalid vanish type '" + string + "'!");
        } else vanishType = VanishType.valueOf(string);

        return vanishType;
    }

    private Map<String, Integer> getItemSlots(List list) {
        Map<String, Integer> guiItemSlots = new HashMap<>();

        for (Object o : list) {
            String guiItemConfig = (String) o;
            guiItemSlots.put(guiItemConfig.split(":")[0], Integer.parseInt(guiItemConfig.split(":")[1]) - 1);
        }
        return guiItemSlots;
    }
}

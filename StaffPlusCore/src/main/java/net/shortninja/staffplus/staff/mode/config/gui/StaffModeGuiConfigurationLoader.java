package net.shortninja.staffplus.staff.mode.config.gui;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class StaffModeGuiConfigurationLoader extends ConfigLoader<List<GuiConfiguration>> {
    @Override
    protected List<GuiConfiguration> load(FileConfiguration config) {
        ConfigurationSection configurationSection = config.getConfigurationSection("staff-mode.custom-gui");
        if (configurationSection == null) {
            return Collections.emptyList();
        }

        return configurationSection.getKeys(false).stream().map(k -> {
            String prefix = "staff-mode.custom-gui." + k + ".";
            String permission = config.getString(prefix + "permission");
            int weight = config.getInt(prefix + "weight");
            return new GuiConfiguration(permission, weight, getItemSlots(config, prefix));
        })
            .sorted(Comparator.comparingInt(GuiConfiguration::getWeight).reversed())
            .collect(Collectors.toList());
    }

    private Map<String, Integer> getItemSlots(FileConfiguration config, String prefix) {
        List list = config.getList(prefix + "gui", new ArrayList<>());
        Map<String, Integer> guiItemSlots = new HashMap<>();

        for (Object o : list) {
            String guiItemConfig = (String) o;
            guiItemSlots.put(guiItemConfig.split(":")[0], Integer.parseInt(guiItemConfig.split(":")[1]) - 1);
        }
        return guiItemSlots;
    }
}

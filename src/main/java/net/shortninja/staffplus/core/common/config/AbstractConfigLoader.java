package net.shortninja.staffplus.core.common.config;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.Sounds;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class AbstractConfigLoader<T> {

    protected FileConfiguration defaultConfig;
    protected FileConfiguration permissionsConfig;
    protected FileConfiguration commandsConfig;
    protected FileConfiguration staffModeModulesConfig;
    protected FileConfiguration staffModeModesConfig;
    protected FileConfiguration staffModeCustomModulesConfig;

    public T loadConfig() {
        defaultConfig = StaffPlus.get().getFileConfigurations().get("config");
        permissionsConfig = StaffPlus.get().getFileConfigurations().get("permissions");
        commandsConfig = StaffPlus.get().getFileConfigurations().get("commands");
        staffModeModulesConfig = StaffPlus.get().getFileConfigurations().get("staffmode-modules");
        staffModeModesConfig = StaffPlus.get().getFileConfigurations().get("staffmode-modes");
        staffModeCustomModulesConfig = StaffPlus.get().getFileConfigurations().get("staffmode-custom-modules");
        return load();
    }

    protected abstract T load();

    protected Sounds stringToSound(String string) {
        Sounds sound = Sounds.ORB_PICKUP;
        boolean isValid = JavaUtils.isValidEnum(Sounds.class, string);
        if (string.equalsIgnoreCase("NONE")) {
            return null;
        }
        if (!isValid) {
            StaffPlus.get().getLogger().warning("Invalid sound name '" + string + "'!");
            return null;
        } else sound = Sounds.valueOf(string);

        return sound;
    }

    protected String sanitize(String string) {
        if (string.contains(":")) {
            string = string.replace(string.substring(string.lastIndexOf(':')), "");
        }

        return string.toUpperCase();
    }
}

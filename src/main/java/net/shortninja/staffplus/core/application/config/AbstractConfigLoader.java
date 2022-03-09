package net.shortninja.staffplus.core.application.config;

import net.shortninja.staffplus.core.StaffPlus;
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

    protected String sanitize(String string) {
        String result = string.toUpperCase();
        if (string.contains(":")) {
            result = result.replace(string.substring(string.lastIndexOf(':')), "");
        }

        return result;
    }
}

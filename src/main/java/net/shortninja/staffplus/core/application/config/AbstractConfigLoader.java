package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.configuration.ConfigurationLoader;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

public abstract class AbstractConfigLoader<T> {

    protected FileConfiguration defaultConfig;
    protected FileConfiguration permissionsConfig;
    protected FileConfiguration commandsConfig;
    protected FileConfiguration staffModeModulesConfig;
    protected FileConfiguration staffModeModesConfig;
    protected FileConfiguration staffModeCustomModulesConfig;

    private final ConfigurationLoader configurationLoader;

    protected AbstractConfigLoader(ConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

    public T loadConfig() {
        defaultConfig = configurationLoader.getConfigurationFiles().get("config");
        permissionsConfig = configurationLoader.getConfigurationFiles().get("permissions");
        commandsConfig = configurationLoader.getConfigurationFiles().get("commands");
        staffModeModulesConfig = configurationLoader.getConfigurationFiles().get("staffmode-modules");
        staffModeModesConfig = configurationLoader.getConfigurationFiles().get("staffmode-modes");
        staffModeCustomModulesConfig = configurationLoader.getConfigurationFiles().get("staffmode-custom-modules");
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

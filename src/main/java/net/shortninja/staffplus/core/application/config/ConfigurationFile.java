package net.shortninja.staffplus.core.application.config;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationFile {

    private String identifier;
    private String path;
    private FileConfiguration fileConfiguration;

    public ConfigurationFile(String path) {
        this.identifier = getConfigId(path);
        this.path = path;
        ConfigurationUtil.saveConfiguration(path);
        this.fileConfiguration = ConfigurationUtil.loadConfiguration(path);
    }

    public ConfigurationFile(String path, FileConfiguration fileConfiguration) {
        this.identifier = getConfigId(path);
        this.path = path;
        this.fileConfiguration = fileConfiguration;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getPath() {
        return path;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    private String getConfigId(String path) {
        return path.replace("/", "-")
            .replace(".yml", "")
            .replace("lang-", "")
            .replace("configuration-", "");
    }
}

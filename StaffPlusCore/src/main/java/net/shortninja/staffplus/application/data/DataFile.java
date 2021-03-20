package net.shortninja.staffplus.application.data;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataFile {
    private MessageCoordinator message;
    private File file;
    private YamlConfiguration configuration;

    public DataFile(String name) {
        message = IocContainer.getMessage();
        file = new File(StaffPlus.get().getDataFolder(), name);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        if (!file.exists()) {
            StaffPlus.get().saveResource(name, false);
        }

        this.load();
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public int getInt(String path) {
        if (configuration.contains(path)) {
            return configuration.getInt(path);
        }

        return 0;
    }

    public boolean getBoolean(String path, boolean isStatus) {
        boolean value = false;

        if (configuration.contains(path)) {
            value = configuration.getBoolean(path);
        }

        return value;
    }

    public String getString(String path) {
        if (configuration.contains(path)) {
            return message.colorize(configuration.getString(path));
        }

        return "null";
    }

}
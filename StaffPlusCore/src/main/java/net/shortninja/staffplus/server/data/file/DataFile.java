package net.shortninja.staffplus.server.data.file;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataFile {
    private MessageCoordinator message;
    private File file;
    private YamlConfiguration configuration;

    public DataFile(String name) {
        message = StaffPlus.get().message;
        file = new File(StaffPlus.get().getDataFolder(), name);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        if (!file.exists()) {
            StaffPlus.get().saveResource(name, false);
        }

        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public double getDouble(String path) {
        if (configuration.contains(path)) {
            return configuration.getDouble(path);
        }

        return 0;
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

    public List<String> getStringList(String path) {
        if (configuration.contains(path)) {
            ArrayList<String> strings = new ArrayList<>();

            for (String string : configuration.getStringList(path)) {
                strings.add(message.colorize(string));
            }

            return strings;
        }

        return Arrays.asList("null");
    }
}
package net.shortninja.staffplus.core.common.config;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.Sounds;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class AbstractConfigLoader<T> {

    public T loadConfig() {
        return load(StaffPlus.get().getConfig());
    }

    protected abstract T load(FileConfiguration config);

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

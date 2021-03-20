package net.shortninja.staffplus.common.config;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.Sounds;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ConfigLoader<T> {

    public T loadConfig() {
        return load(StaffPlus.get().getConfig());
    }

    protected abstract T load(FileConfiguration config);

    protected Sounds stringToSound(String string) {
        Sounds sound = Sounds.ORB_PICKUP;
        boolean isValid = JavaUtils.isValidEnum(Sounds.class, string);

        if (!isValid) {
            IocContainer.getMessage().sendConsoleMessage("Invalid sound name '" + string + "'!", true);
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

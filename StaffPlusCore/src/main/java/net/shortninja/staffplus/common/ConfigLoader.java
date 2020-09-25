package net.shortninja.staffplus.common;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.Sounds;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ConfigLoader<T> {

    protected static final FileConfiguration config = StaffPlus.get().getConfig();

    public abstract T load();

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

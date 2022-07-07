package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.Sounds;
import org.bukkit.Sound;

public class SoundsConfigTransformer implements IConfigTransformer<Sounds, String> {
    @Override
    public Sounds mapConfig(String configValue) {
        return stringToSound(sanitize(configValue));
    }

    private Sounds stringToSound(String string) {
        boolean isValid = JavaUtils.isValidEnum(Sound.class, string);
        if (string.equalsIgnoreCase("NONE")) {
            return null;
        }

        if (!isValid) {
            StaffPlusPlus.get().getLogger().warning("Invalid sound name '" + string + "'!");
            return null;
        } else {
            return new Sounds(string);
        }
    }


    private String sanitize(String string) {
        if (string.contains(":")) {
            string = string.replace(string.substring(string.lastIndexOf(':')), "");
        }

        return string.toUpperCase();
    }
}

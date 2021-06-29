package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.Sounds;

public class SoundsConfigTransformer implements IConfigTransformer<Sounds, String> {
    @Override
    public Sounds mapConfig(String configValue) {
        return stringToSound(sanitize(configValue));
    }

    private Sounds stringToSound(String string) {
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


    private String sanitize(String string) {
        if (string.contains(":")) {
            string = string.replace(string.substring(string.lastIndexOf(':')), "");
        }

        return string.toUpperCase();
    }
}

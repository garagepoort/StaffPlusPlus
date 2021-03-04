package be.garagepoort.staffplusplus.discord.common;

import org.bukkit.Location;

/**
 * @author Shortninja, DarkSeraphim, ...
 */

public class JavaUtils {

    /**
     * "Serializes" the Location with simple string concatenation.
     *
     * @param location The Location to serialize.
     * @return String in the format of "x, y, z".
     */
    public static String serializeLocation(Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }
}
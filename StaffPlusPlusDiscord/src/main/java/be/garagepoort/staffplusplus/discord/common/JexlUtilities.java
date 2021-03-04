package be.garagepoort.staffplusplus.discord.common;

import org.bukkit.Location;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JexlUtilities {

    public static String parseTime(ZonedDateTime zonedDateTime) {
        LocalDateTime endDateTime = LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC);
        return endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String parseLocation(String serverName, Location location) {
        serverName = serverName == null ? "Unknown server" : serverName;
        return serverName + "\\\\n" + location.getWorld().getName() + " | " + JavaUtils.serializeLocation(location);
    }

    public static String parseLocation(Location location) {
        return location.getWorld().getName() + " | " + JavaUtils.serializeLocation(location);
    }
}

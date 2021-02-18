package be.garagepoort.staffplusplus.discord.common;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JexlUtilities {

    public static String parseTime(ZonedDateTime zonedDateTime) {
        LocalDateTime endDateTime = LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC);
        return endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}

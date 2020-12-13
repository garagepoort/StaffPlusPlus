package net.shortninja.staffplus.common.time;

import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.util.lib.JavaUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum TimeUnit {
    SECOND(ChronoUnit.SECONDS),
    MINUTE(ChronoUnit.MINUTES),
    HOUR(ChronoUnit.HOURS),
    DAY(ChronoUnit.SECONDS),
    WEEK(ChronoUnit.WEEKS),
    MONTH(ChronoUnit.MONTHS),
    YEAR(ChronoUnit.YEARS);

    private final TemporalUnit temporalUnit;

    TimeUnit(TemporalUnit temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public static long getDuration(String un, int time) {
        if (!JavaUtils.isValidEnum(TimeUnit.class, un)) {
            throw new BusinessException("&CInvalid time unit used. Valid values: ["+ Arrays.stream(TimeUnit.values()).map(Enum::name).collect(Collectors.joining(", "))+"]");
        }
        TimeUnit timeUnit = TimeUnit.valueOf(un.toUpperCase());
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plus(time, timeUnit.getTemporalUnit());
        long fromMillis = ZonedDateTime.of(from, ZoneId.systemDefault()).toInstant().toEpochMilli();
        long toMillis = ZonedDateTime.of(to, ZoneId.systemDefault()).toInstant().toEpochMilli();
        return toMillis - fromMillis;
    }

    private TemporalUnit getTemporalUnit() {
        return temporalUnit;
    }
}
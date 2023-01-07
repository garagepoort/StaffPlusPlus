package net.shortninja.staffplus.core.common.time;

import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum TimeUnitShort {
    s(ChronoUnit.SECONDS),
    m(ChronoUnit.MINUTES),
    h(ChronoUnit.HOURS),
    d(ChronoUnit.DAYS),
    w(ChronoUnit.WEEKS),
    M(ChronoUnit.MONTHS),
    y(ChronoUnit.YEARS);

    private final TemporalUnit temporalUnit;

    TimeUnitShort(TemporalUnit temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public static long getDurationFromString(String duration) {
        String amountString = duration.substring(0, duration.length() - 1);
        String timeUnit = duration.substring(duration.length() - 1);
        int amount = Integer.parseInt(amountString);
        return TimeUnitShort.getDuration(timeUnit, amount);
    }

    public static long getDuration(String un, int time) {
        if (!JavaUtils.isValidEnum(TimeUnitShort.class, un)) {
            throw new BusinessException("&CInvalid time unit used. Valid values: ["+ Arrays.stream(TimeUnitShort.values()).map(Enum::name).collect(Collectors.joining(", "))+"]");
        }
        TimeUnitShort timeUnit = TimeUnitShort.valueOf(un);
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
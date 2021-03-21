package net.shortninja.staffplus.common.time;

import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.exceptions.BusinessException;

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
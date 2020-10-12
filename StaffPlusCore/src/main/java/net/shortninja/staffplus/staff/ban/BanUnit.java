package net.shortninja.staffplus.staff.ban;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum BanUnit {
	SECOND(ChronoUnit.SECONDS),
    MINUTE(ChronoUnit.MINUTES),
    HOUR(ChronoUnit.HOURS),
    DAY(ChronoUnit.SECONDS),
    WEEK(ChronoUnit.WEEKS),
    MONTH(ChronoUnit.MONTHS),
    YEAR(ChronoUnit.YEARS);

	private final TemporalUnit temporalUnit;

	BanUnit(TemporalUnit temporalUnit){
		this.temporalUnit = temporalUnit;
	}

	public static long getTicks(String un, int time){
	    BanUnit banUnit = BanUnit.valueOf(un.toUpperCase());
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plus(time, banUnit.getTemporalUnit());
        long fromMillis = ZonedDateTime.of(from, ZoneId.systemDefault()).toInstant().toEpochMilli();
        long toMillis = ZonedDateTime.of(to, ZoneId.systemDefault()).toInstant().toEpochMilli();
		return toMillis - fromMillis;
	}

    private TemporalUnit getTemporalUnit() {
        return temporalUnit;
    }
}
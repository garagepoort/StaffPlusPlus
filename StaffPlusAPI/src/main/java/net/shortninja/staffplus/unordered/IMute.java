package net.shortninja.staffplus.unordered;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface IMute {

    int getId();

    Long getCreationTimestamp();

    ZonedDateTime getCreationDate();

    String getReason();

    String getPlayerName();

    UUID getPlayerUuid();

    String getIssuerName();

    UUID getIssuerUuid();

    String getUnmutedByName();

    UUID getUnmutedByUuid();

    Long getEndTimestamp();

    ZonedDateTime getEndDate();

    String getUnmuteReason();

    String getHumanReadableDuration();
}

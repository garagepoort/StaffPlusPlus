package net.shortninja.staffplus.unordered;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface IBan {

    int getId();

    Long getCreationTimestamp();

    ZonedDateTime getCreationDate();

    String getReason();

    String getPlayerName();

    UUID getPlayerUuid();

    String getIssuerName();

    UUID getIssuerUuid();

    String getUnbannedByName();

    UUID getUnbannedByUuid();

    Long getEndTimestamp();

    ZonedDateTime getEndDate();

    String getUnbanReason();

    String getHumanReadableDuration();
}

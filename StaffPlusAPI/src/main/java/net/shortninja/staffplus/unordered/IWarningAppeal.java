package net.shortninja.staffplus.unordered;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface IWarningAppeal {

    int getId();

    UUID getAppealerUuid();

    String getAppealerName();

    String getResolverName();

    UUID getResolverUuid();

    AppealStatus getStatus();

    String getReason();

    Long getCreationTimestamp();

    ZonedDateTime getCreationDate();
}

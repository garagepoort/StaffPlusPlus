package net.shortninja.staffplus.unordered;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IWarning extends Actionable {

    String getReason();

    String getIssuerName();

    UUID getIssuerUuid();

    void setIssuerName(String newName);

    UUID getUuid();

    int getId();

    boolean shouldRemove();

    String getName();

    int getScore();

    String getSeverity();

    ZonedDateTime getTimestamp();

    String getServerName();

    Optional<? extends IWarningAppeal> getAppeal();
}

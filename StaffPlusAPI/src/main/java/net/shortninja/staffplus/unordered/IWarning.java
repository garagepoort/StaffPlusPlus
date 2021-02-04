package net.shortninja.staffplus.unordered;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface IWarning {

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
}

package net.shortninja.staffplus.unordered;

import java.util.UUID;

public interface IWarning {

    String getReason();

    String getIssuerName();

    UUID getIssuerUuid();

    long getTime();

    void setIssuerName(String newName);

    UUID getUuid();

    int getId();

    boolean shouldRemove();

    String getName();

    int getScore();

    String getSeverity();
}

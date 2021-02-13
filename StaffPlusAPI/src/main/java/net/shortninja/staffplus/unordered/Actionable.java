package net.shortninja.staffplus.unordered;

import java.util.UUID;

public interface Actionable {

    int getId();

    String getActionableType();

    UUID getTargetUuid();
}

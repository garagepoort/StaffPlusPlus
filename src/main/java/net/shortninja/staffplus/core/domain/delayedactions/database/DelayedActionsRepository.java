package net.shortninja.staffplus.core.domain.delayedactions.database;

import net.shortninja.staffplus.core.domain.delayedactions.DelayedAction;
import net.shortninja.staffplus.core.domain.delayedactions.Executor;

import java.util.List;
import java.util.UUID;

public interface DelayedActionsRepository {

    void saveDelayedAction(UUID uuid, String command, Executor executor, String serverName);

    void saveDelayedAction(UUID uuid, String command, Executor executor);

    void saveDelayedAction(UUID uuid, String command, Executor executor, int executableActionId, boolean rollback);

    List<DelayedAction> getDelayedActions(UUID uuid);

    void clearDelayedActions(UUID uuid);
}

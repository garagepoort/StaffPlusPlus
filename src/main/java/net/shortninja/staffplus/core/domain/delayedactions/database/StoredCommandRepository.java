package net.shortninja.staffplus.core.domain.delayedactions.database;

import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;
import net.shortninja.staffplusplus.Actionable;

import java.util.List;
import java.util.UUID;

public interface StoredCommandRepository {

    List<StoredCommandEntity> getCommandsFor(Actionable actionable);

    List<StoredCommandEntity> getCommandsFor(Actionable actionable, String group);

    int saveCommand(StoredCommandEntity storedCommandEntity);

    List<StoredCommandEntity> getDelayedActions(UUID uuid);

    void markExecuted(int id);

    void markDelayed(int id);

    void markRollbacked(int id);
}

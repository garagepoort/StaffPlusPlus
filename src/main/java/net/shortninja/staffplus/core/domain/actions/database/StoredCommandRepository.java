package net.shortninja.staffplus.core.domain.actions.database;

import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;
import net.shortninja.staffplusplus.Actionable;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface StoredCommandRepository {

    void save(Connection connection, List<StoredCommandEntity> commandEntities);

    List<StoredCommandEntity> getCommandsFor(Actionable actionable);

    int saveCommand(StoredCommandEntity storedCommandEntity);

    List<StoredCommandEntity> getDelayedActions(UUID uuid);

    void markExecuted(int id);

    void markDelayed(int id);

    void markRollbacked(int id);

    void deleteExecutedCommands();

    void deleteAllFromActionable(int actionable_id);
}

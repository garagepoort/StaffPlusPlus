package net.shortninja.staffplus.core.domain.actions.database;

import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;
import net.shortninja.staffplusplus.Actionable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface StoredCommandRepository {

    int save(StoredCommandEntity commandEntity, Connection sql) throws SQLException;

    List<StoredCommandEntity> getCommandsFor(Actionable actionable);

    int saveCommand(StoredCommandEntity storedCommandEntity);

    List<StoredCommandEntity> getDelayedActions(UUID uuid);

    void markExecuted(int id);

    void markDelayed(int id);

    void markRollbacked(int id);

    void deleteExecutedCommands();

    void deleteAllFromActionable(int actionable_id);
}

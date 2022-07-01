package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.domain.actions.ActionRunStrategy;
import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;
import net.shortninja.staffplus.core.domain.actions.database.StoredCommandRepository;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@IocBean
@IocMultiProvider(Migration.class)
public class V72_MigrateOldActionableActionsTableMigration implements Migration {

    private final QueryBuilderFactory query;
    private final StoredCommandRepository storedCommandRepository;

    public V72_MigrateOldActionableActionsTableMigration(QueryBuilderFactory query, StoredCommandRepository storedCommandRepository) {
        this.query = query;
        this.storedCommandRepository = storedCommandRepository;
    }

    @Override
    public List<String> getStatements() {
        List<StoredCommandEntity> storedCommandEntities = query.create().find("SELECT * FROM sp_actionable_actions;", this::mapOldCommandEntity);
        storedCommandRepository.save(storedCommandEntities);
        return Collections.emptyList();
    }

    @NotNull
    private StoredCommandEntity mapOldCommandEntity(ResultSet rs) throws SQLException {
        int actionableId = rs.getInt("actionable_id");
        String actionableType = rs.getString("actionable_type");

        String command = rs.getString("command");
        String rollbackCommand = rs.getString("rollback_command");

        Long executionTimestamp = rs.getLong("execution_timestamp");
        executionTimestamp = rs.wasNull() ? null : executionTimestamp;
        Long rollbackTimestamp = rs.getLong("rollback_timestamp");
        rollbackTimestamp = rs.wasNull() ? null : rollbackTimestamp;

        StoredCommandEntity rollbackCommandEntity = null;
        if (rollbackCommand != null) {
            rollbackCommandEntity = getRollbackCommandEntity(rollbackCommand, rollbackTimestamp);
        }
        return getStoredCommandEntity(actionableId, actionableType, command, executionTimestamp, rollbackTimestamp, rollbackCommandEntity);
    }

    @NotNull
    private StoredCommandEntity getStoredCommandEntity(int actionableId, String actionableType, String command, Long executionTimestamp, Long rollbackTimestamp, StoredCommandEntity rollbackCommandEntity) {
        return new StoredCommandEntity(
            null,
            command,
            Constants.CONSOLE_UUID,
            null,
            ActionRunStrategy.ONLINE,
            null,
            System.currentTimeMillis(),
            executionTimestamp,
            rollbackTimestamp,
            actionableId,
            actionableType,
            null,
            rollbackCommandEntity,
            false
        );
    }

    @NotNull
    private StoredCommandEntity getRollbackCommandEntity(String rollbackCommand, Long rollbackTimestamp) {
        return new StoredCommandEntity(
            null,
            rollbackCommand,
            Constants.CONSOLE_UUID,
            null,
            ActionRunStrategy.ONLINE,
            null,
            System.currentTimeMillis(),
            rollbackTimestamp,
            null,
            null,
            null,
            null,
            null,
            false
        );
    }

    @Override
    public int getVersion() {
        return 72;
    }
}

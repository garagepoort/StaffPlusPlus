package net.shortninja.staffplus.core.domain.actions.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilder;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.actions.ActionRunStrategy;
import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;
import net.shortninja.staffplusplus.Actionable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@IocBean
public class SqlStoredCommandRepository implements StoredCommandRepository {

    private final String serverNameFilter;
    private final StoredCommandSqlMapper storedCommandSqlMapper;
    private final QueryBuilderFactory query;

    public SqlStoredCommandRepository(Options options, StoredCommandSqlMapper storedCommandSqlMapper, QueryBuilderFactory query) {
        serverNameFilter = "AND (sp_commands.server_name is null OR sp_commands.server_name='" + options.serverName + "')";
        this.storedCommandSqlMapper = storedCommandSqlMapper;
        this.query = query;
    }

    @Override
    public int saveCommand(StoredCommandEntity commandEntity) {
        QueryBuilder query = this.query.create().startTransaction();
        int commandId = saveOne(commandEntity, query);
        query.commit();
        return commandId;
    }

    @Override
    public void save(List<StoredCommandEntity> commandEntities) {
        QueryBuilder query = this.query.create().startTransaction();
        commandEntities.forEach(c -> saveOne(c, query));
        query.commit();
    }

    private int saveOne(StoredCommandEntity commandEntity, QueryBuilder query) {
        return query.insertQuery("INSERT INTO sp_commands(executor_uuid, executor_run_strategy, target_uuid, target_run_strategy, command, creation_timestamp, execution_timestamp, server_name, is_delayed, rollback_command_id, actionable_id, actionable_type) " +
            " VALUES(? ,?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?);", insert -> {
            insert.setString(1, commandEntity.getExecutorUuid().toString());
            insert.setString(2, ActionRunStrategy.ALWAYS.toString());
            insertIfPresent(insert, 3, commandEntity.getTargetUuid().map(UUID::toString), Types.VARCHAR);
            insertIfPresent(insert, 4, commandEntity.getTargetRunStrategy().map(Enum::name), Types.VARCHAR);
            insert.setString(5, commandEntity.getCommand());
            insert.setLong(6, System.currentTimeMillis());
            insertIfPresent(insert, 7, commandEntity.getExecutionTimestamp(), Types.BIGINT);
            insertIfPresent(insert, 8, commandEntity.getServerName(), Types.VARCHAR);
            insert.setBoolean(9, commandEntity.isDelayed());

            if (commandEntity.getRollbackCommand().isPresent()) {
                int rollbackId = saveOne(commandEntity.getRollbackCommand().get(), query);
                insert.setInt(10, rollbackId);
            } else {
                insert.setNull(10, Types.INTEGER);
            }
            insertIfPresent(insert, 11, commandEntity.getActionableId(), Types.INTEGER);
            insertIfPresent(insert, 12, commandEntity.getActionableType(), Types.VARCHAR);
        });
    }

    private void insertIfPresent(PreparedStatement insert, int i, Optional optional, int sqlType) throws SQLException {
        if (optional.isPresent()) {
            insert.setObject(i, optional.get());
        } else {
            insert.setNull(i, sqlType);
        }
    }

    @Override
    public List<StoredCommandEntity> getCommandsFor(Actionable actionable) {
        return this.query.create().find("SELECT * FROM sp_commands " +
                "LEFT OUTER JOIN sp_commands rollbackcommand on sp_commands.rollback_command_id = rollbackcommand.id " +
                "WHERE sp_commands.actionable_id = ? AND sp_commands.actionable_type = ? " + serverNameFilter + " " +
                "ORDER BY sp_commands.creation_timestamp ASC",
            ps -> {
                ps.setInt(1, actionable.getId());
                ps.setString(2, actionable.getActionableType());
            }, storedCommandSqlMapper::map);
    }

    @Override
    public List<StoredCommandEntity> getDelayedActions(UUID uuid) {
        return this.query.create().find("SELECT * FROM sp_commands " +
                "LEFT OUTER JOIN sp_commands rollbackcommand on sp_commands.rollback_command_id = rollbackcommand.id " +
                "WHERE ((sp_commands.executor_uuid = ? AND sp_commands.executor_run_strategy = ?) " +
                "OR (sp_commands.target_uuid = ? AND sp_commands.target_run_strategy = ?)) " +
                "AND sp_commands.execution_timestamp IS NULL " +
                "AND sp_commands.is_delayed = ? " + serverNameFilter + " " +
                "ORDER BY sp_commands.creation_timestamp ASC",
            ps -> {
                ps.setString(1, uuid.toString());
                ps.setString(2, ActionRunStrategy.DELAY.toString());
                ps.setString(3, uuid.toString());
                ps.setString(4, ActionRunStrategy.DELAY.toString());
                ps.setBoolean(5, true);
            },
            storedCommandSqlMapper::map);
    }

    @Override
    public void markExecuted(int executableActionId) {
        this.query.create().updateQuery("UPDATE sp_commands set execution_timestamp=? WHERE id=?;",
            insert -> {
                insert.setLong(1, System.currentTimeMillis());
                insert.setInt(2, executableActionId);
            });
    }

    @Override
    public void markDelayed(int executableActionId) {
        this.query.create().updateQuery("UPDATE sp_commands set is_delayed=? WHERE id=?;",
            insert -> {
                insert.setBoolean(1, true);
                insert.setInt(2, executableActionId);
            });
    }

    @Override
    public void markRollbacked(int id) {
        this.query.create().updateQuery("UPDATE sp_commands set rollback_timestamp=? WHERE id=?;",
            insert -> {
                insert.setLong(1, System.currentTimeMillis());
                insert.setInt(2, id);
            });
    }

    @Override
    public void deleteExecutedCommands() {
        this.query.create().deleteQuery("DELETE FROM sp_commands WHERE " +
            "execution_timestamp IS NOT NULL " +
            "AND (rollback_command_id IS NULL OR rollback_timestamp IS NOT NULL) " +
            "AND actionable_id IS NOT NULL;", d -> {
        });
    }

    @Override
    public void deleteAllFromActionable(int actionable_id) {
        QueryBuilder query = this.query.create().startTransaction();
        query.deleteQuery("DELETE FROM sp_commands WHERE id in (SELECT rollback_command_id from sp_commands where actionable_id = ?);", ps -> ps.setInt(1, actionable_id));
        query.deleteQuery("DELETE FROM sp_commands WHERE actionable_id = ?;", ps -> ps.setInt(1, actionable_id));
        query.commit();
    }
}

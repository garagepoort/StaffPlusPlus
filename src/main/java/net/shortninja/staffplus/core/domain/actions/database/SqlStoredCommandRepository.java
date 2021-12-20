package net.shortninja.staffplus.core.domain.actions.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.database.SqlRepository;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.actions.ActionRunStrategy;
import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;
import net.shortninja.staffplusplus.Actionable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@IocBean
public class SqlStoredCommandRepository extends SqlRepository implements StoredCommandRepository {

    protected final Options options;
    protected final String serverNameFilter;
    private final SqlConnectionProvider sqlConnectionProvider;
    private final StoredCommandSqlMapper storedCommandSqlMapper;

    public SqlStoredCommandRepository(Options options, SqlConnectionProvider sqlConnectionProvider, StoredCommandSqlMapper storedCommandSqlMapper) {
        super(sqlConnectionProvider);
        serverNameFilter = "AND (sp_commands.server_name is null OR sp_commands.server_name='" + options.serverName + "')";
        this.options = options;
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.storedCommandSqlMapper = storedCommandSqlMapper;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public int saveCommand(StoredCommandEntity commandEntity) {
        try (Connection sql = getConnection()) {
            sql.setAutoCommit(false);
            int save = save(commandEntity, sql);
            sql.setAutoCommit(true);
            return save;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int save(StoredCommandEntity commandEntity, Connection sql) throws SQLException {
        PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_commands(executor_uuid, executor_run_strategy, target_uuid, target_run_strategy, command, creation_timestamp, execution_timestamp, server_name, is_delayed, rollback_command_id, actionable_id, actionable_type) " +
            " VALUES(? ,?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        sql.setAutoCommit(false);
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
            int rollbackId = save(commandEntity.getRollbackCommand().get(), sql);
            insert.setInt(10, rollbackId);
        } else {
            insert.setNull(10, Types.INTEGER);
        }
        insertIfPresent(insert, 11, commandEntity.getActionableId(), Types.INTEGER);
        insertIfPresent(insert, 12, commandEntity.getActionableType(), Types.VARCHAR);
        insert.executeUpdate();
        return getGeneratedId(sql, insert);
    }


    @Override
    public List<StoredCommandEntity> getCommandsFor(Actionable actionable) {
        List<StoredCommandEntity> actions = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_commands " +
                 "LEFT OUTER JOIN sp_commands rollbackcommand on sp_commands.rollback_command_id = rollbackcommand.id " +
                 "WHERE sp_commands.actionable_id = ? AND sp_commands.actionable_type = ? " + serverNameFilter + " " +
                 "ORDER BY sp_commands.creation_timestamp ASC")
        ) {
            ps.setInt(1, actionable.getId());
            ps.setString(2, actionable.getActionableType());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    actions.add(storedCommandSqlMapper.map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return actions;
    }

    @Override
    public List<StoredCommandEntity> getDelayedActions(UUID uuid) {
        List<StoredCommandEntity> actions = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_commands " +
                 "LEFT OUTER JOIN sp_commands rollbackcommand on sp_commands.rollback_command_id = rollbackcommand.id " +
                 "WHERE ((sp_commands.executor_uuid = ? AND sp_commands.executor_run_strategy = ?) " +
                 "OR (sp_commands.target_uuid = ? AND sp_commands.target_run_strategy = ?)) " +
                 "AND sp_commands.execution_timestamp IS NULL " +
                 "AND sp_commands.is_delayed = ? " + serverNameFilter + " " +
                 "ORDER BY sp_commands.creation_timestamp ASC")
        ) {
            ps.setString(1, uuid.toString());
            ps.setString(2, ActionRunStrategy.DELAY.toString());
            ps.setString(3, uuid.toString());
            ps.setString(4, ActionRunStrategy.DELAY.toString());
            ps.setBoolean(5, true);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    actions.add(storedCommandSqlMapper.map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return actions;
    }

    @Override
    public void markExecuted(int executableActionId) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_commands set execution_timestamp=? WHERE id=?;")) {
            insert.setLong(1, System.currentTimeMillis());
            insert.setInt(2, executableActionId);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void markDelayed(int executableActionId) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_commands set is_delayed=? WHERE id=?;")) {
            insert.setBoolean(1, true);
            insert.setInt(2, executableActionId);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void markRollbacked(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_commands set rollback_timestamp=? WHERE id=?;")) {
            insert.setLong(1, System.currentTimeMillis());
            insert.setInt(2, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteExecutedCommands() {
        try (Connection sql = getConnection();
             PreparedStatement delete = sql.prepareStatement("DELETE FROM sp_commands WHERE " +
                 "execution_timestamp IS NOT NULL " +
                 "AND (rollback_command_id IS NULL OR rollback_timestamp IS NOT NULL) " +
                 "AND actionable_id IS NOT NULL;")) {
            delete.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteAllFromActionable(int actionable_id) {
        try (Connection sql = getConnection()) {
            sql.setAutoCommit(false);
            PreparedStatement deleteRollbackCommands = sql.prepareStatement("DELETE FROM sp_commands WHERE id in (SELECT rollback_command_id from sp_commands where actionable_id = ?);");
            deleteRollbackCommands.setInt(1, actionable_id);
            deleteRollbackCommands.executeUpdate();

            PreparedStatement delete = sql.prepareStatement("DELETE FROM sp_commands WHERE actionable_id = ?;");
            delete.setInt(1, actionable_id);
            delete.executeUpdate();
            sql.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

package net.shortninja.staffplus.core.domain.delayedactions.database;

import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.delayedactions.DelayedAction;
import net.shortninja.staffplus.core.domain.delayedactions.Executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractSqlDelayedActionsRepository implements DelayedActionsRepository {

    private final Options options;
    private final SqlConnectionProvider sqlConnectionProvider;
    private final String serverNameFilter;

    public AbstractSqlDelayedActionsRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        this.options = options;
        serverNameFilter = "AND (server_name is null OR server_name='" + options.serverName + "')";
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public void saveDelayedAction(UUID uuid, String command, Executor executor, String serverName) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_delayed_actions(Player_UUID, command, timestamp, server_name, executor) " +
                 "VALUES(? ,?, ?, ?, ?);")) {
            insert.setString(1, uuid.toString());
            insert.setString(2, command);
            insert.setLong(3, System.currentTimeMillis());
            insert.setString(4, serverName);
            insert.setString(5, executor.name());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void saveDelayedAction(UUID uuid, String command, Executor executor) {
        saveDelayedAction(uuid, command, executor, options.serverName);
    }

    @Override
    public void saveDelayedAction(UUID uuid, String command, Executor executor, int executableActionId, boolean rollback) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_delayed_actions(Player_UUID, command, timestamp, server_name, executable_action_id, rollback, executor) " +
                 "VALUES(? ,?, ?, ?, ?, ?, ?);")) {
            insert.setString(1, uuid.toString());
            insert.setString(2, command);
            insert.setLong(3, System.currentTimeMillis());
            insert.setString(4, options.serverName);
            insert.setInt(5, executableActionId);
            insert.setBoolean(6, rollback);
            insert.setString(7, executor.name());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<DelayedAction> getDelayedActions(UUID uuid) {
        List<DelayedAction> actions = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT command, executable_action_id, rollback, executor FROM sp_delayed_actions WHERE Player_UUID = ? " + serverNameFilter + " ORDER BY timestamp ASC")
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String command = rs.getString("command");
                    Executor executor = Executor.valueOf(rs.getString("executor"));
                    boolean rollback = rs.getBoolean("rollback");
                    Integer executableActionId = rs.getInt("executable_action_id");
                    if (rs.wasNull()) {
                        executableActionId = null;
                    }
                    DelayedAction delayedAction = new DelayedAction(command, executableActionId, executor, rollback);
                    actions.add(delayedAction);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return actions;
    }

    @Override
    public void clearDelayedActions(UUID uuid) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_delayed_actions WHERE Player_UUID = ? " + serverNameFilter);) {
            insert.setString(1, uuid.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

package net.shortninja.staffplus.staff.delayedactions;

import net.shortninja.staffplus.server.data.config.Options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractSqlDelayedActionsRepository implements DelayedActionsRepository {

    private final Options options;
    private final String serverNameFilter;

    protected AbstractSqlDelayedActionsRepository(Options options) {
        this.options = options;
        serverNameFilter = "AND (server_name is null OR server_name='" + options.serverName + "')";
    }

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public void saveDelayedAction(UUID uuid, String command) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_delayed_actions(Player_UUID, command, timestamp, server_name) " +
                 "VALUES(? ,?, ?, ?);")) {
            insert.setString(1, uuid.toString());
            insert.setString(2, command);
            insert.setLong(3, System.currentTimeMillis());
            insert.setString(4, options.serverName);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveDelayedAction(UUID uuid, String command, int executableActionId, boolean rollback) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_delayed_actions(Player_UUID, command, timestamp, server_name, executable_action_id, rollback) " +
                 "VALUES(? ,?, ?, ?, ?, ?);")) {
            insert.setString(1, uuid.toString());
            insert.setString(2, command);
            insert.setLong(3, System.currentTimeMillis());
            insert.setString(4, options.serverName);
            insert.setInt(5, executableActionId);
            insert.setBoolean(6, rollback);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DelayedAction> getDelayedActions(UUID uuid) {
        List<DelayedAction> actions = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT command, executable_action_id FROM sp_delayed_actions WHERE Player_UUID = ? " + serverNameFilter + " ORDER BY timestamp ASC")
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String command = rs.getString("command");
                    boolean rollback = rs.getBoolean("rollback");
                    Integer executableActionId = rs.getInt("executable_action_id");
                    if(rs.wasNull()) {
                        executableActionId = null;
                    }
                    DelayedAction delayedAction = new DelayedAction(command, executableActionId, rollback);
                    actions.add(delayedAction);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }
}

package net.shortninja.staffplus.actions.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractSqlDelayedActionsRepository implements DelayedActionsRepository {

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public void saveDelayedAction(UUID uuid, String command) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_delayed_actions(Player_UUID, command) " +
                     "VALUES(? ,?);")) {
            insert.setString(1, uuid.toString());
            insert.setString(2, command);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getDelayedActions(UUID uuid) {
        List<String> actions = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT command FROM sp_delayed_actions WHERE Player_UUID = ?")
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    actions.add(rs.getString("command"));
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
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_delayed_actions WHERE Player_UUID = ?");) {
            insert.setString(1, uuid.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

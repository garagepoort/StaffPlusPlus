package net.shortninja.staffplus.domain.actions.database;

import net.shortninja.staffplus.domain.actions.ActionRunStrategy;
import net.shortninja.staffplus.domain.actions.ExecutableActionEntity;
import net.shortninja.staffplusplus.Actionable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlActionableRepository implements ActionableRepository {

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public List<ExecutableActionEntity> getActions(Actionable actionable) {
        List<ExecutableActionEntity> actions = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_actionable_actions WHERE actionable_id=?")) {
            ps.setInt(1, actionable.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    actions.add(buildAction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actions;
    }

    @Override
    public void markExecuted(int executableActionId) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_actionable_actions set execution_timestamp=? WHERE id=?;")) {
            insert.setLong(1, System.currentTimeMillis());
            insert.setInt(2, executableActionId);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void markRollbacked(int executableActionId) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_actionable_actions set rollback_timestamp=? WHERE id=?;")) {
            insert.setLong(1, System.currentTimeMillis());
            insert.setInt(2, executableActionId);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Actionable actionable) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_actionable_actions WHERE actionable_id = ? AND actionable_type=?");) {
            insert.setInt(1, actionable.getId());
            insert.setString(2, actionable.getActionableType());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExecutableActionEntity buildAction(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        int actionableId = rs.getInt("actionable_id");
        String actionableType = rs.getString("actionable_type");

        String command = rs.getString("command");
        String rollbackCommand = rs.getString("rollback_command");
        ActionRunStrategy runStrategy = ActionRunStrategy.valueOf(rs.getString("run_strategy"));
        ActionRunStrategy rollbackRunStrategy = ActionRunStrategy.valueOf(rs.getString("rollback_run_strategy"));

        Long executionTimestamp = rs.getLong("execution_timestamp");
        executionTimestamp = rs.wasNull() ? null : executionTimestamp;
        Long rollbackTimestamp = rs.getLong("rollback_timestamp");
        rollbackTimestamp = rs.wasNull() ? null : rollbackTimestamp;

        return new ExecutableActionEntity(
            id,
            command,
            rollbackCommand,
            runStrategy,
            rollbackRunStrategy,
            executionTimestamp,
            rollbackTimestamp,
            actionableId,
            actionableType);
    }

}

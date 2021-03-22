package net.shortninja.staffplus.core.domain.actions.database;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.actions.ExecutableActionEntity;

import java.sql.*;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlActionableRepository extends AbstractSqlActionableRepository {

    public MysqlActionableRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }

    @Override
    public int saveActionable(ExecutableActionEntity action) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_actionable_actions(actionable_id, actionable_type, command, rollback_command, run_strategy, rollback_run_strategy, execution_timestamp) " +
                 "VALUES(?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setInt(1, action.getActionableId());
            insert.setString(2, action.getActionableType());
            insert.setString(3, action.getCommand());
            insert.setString(4, action.getRollbackCommand());
            insert.setString(5, action.getRunStrategy().name());
            insert.setString(6, action.getRollbackRunStrategy().name());
            if(action.getExecutionTimestamp() != null) {
                insert.setLong(7, action.getExecutionTimestamp());
            } else {
                insert.setNull(7, Types.BIGINT);
            }
            insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

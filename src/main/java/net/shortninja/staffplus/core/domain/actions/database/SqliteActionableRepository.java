package net.shortninja.staffplus.core.domain.actions.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.actions.ExecutableActionEntity;
import org.apache.commons.lang.StringUtils;

import java.sql.*;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteActionableRepository extends AbstractSqlActionableRepository {

    public SqliteActionableRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }

    @Override
    public int saveActionable(ExecutableActionEntity action) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_actionable_actions(actionable_id, actionable_type, command, rollback_command, run_strategy, rollback_run_strategy, execution_timestamp) " +
                 "VALUES(?, ?, ?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setInt(1, action.getActionableId());
            insert.setString(2, action.getActionableType());
            insert.setString(3, action.getCommand());
            if (StringUtils.isNotEmpty(action.getRollbackCommand())) {
                insert.setString(4, action.getRollbackCommand());
            } else {
                insert.setNull(4, Types.VARCHAR);
            }
            insert.setString(5, action.getRunStrategy().name());
            insert.setString(6, action.getRollbackRunStrategy().name());
            if (action.getExecutionTimestamp() != null) {
                insert.setLong(7, action.getExecutionTimestamp());
            } else {
                insert.setNull(7, Types.BIGINT);
            }
            insert.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }
            connection.commit(); // Commits transaction.

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

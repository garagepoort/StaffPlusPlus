package net.shortninja.staffplus.staff.delayedactions;

import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteDelayedActionsRepository extends AbstractSqlDelayedActionsRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }
}

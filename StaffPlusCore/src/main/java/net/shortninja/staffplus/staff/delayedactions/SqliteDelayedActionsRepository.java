package net.shortninja.staffplus.staff.delayedactions;

import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteDelayedActionsRepository extends AbstractSqlDelayedActionsRepository {

    public SqliteDelayedActionsRepository(Options options) {
        super(options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }
}

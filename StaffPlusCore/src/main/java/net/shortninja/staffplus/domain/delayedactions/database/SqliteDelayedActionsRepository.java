package net.shortninja.staffplus.domain.delayedactions.database;

import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.application.database.migrations.sqlite.SqlLiteConnection;

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

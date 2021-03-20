package net.shortninja.staffplus.domain.delayedactions.database;

import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDelayedActionsRepository extends AbstractSqlDelayedActionsRepository {

    public MysqlDelayedActionsRepository(Options options) {
        super(options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}

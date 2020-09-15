package net.shortninja.staffplus.actions.database;

import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDelayedActionsRepository extends AbstractSqlDelayedActionsRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}

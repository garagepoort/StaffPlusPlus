package net.shortninja.staffplus.warn.database;

import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlWarnRepository extends AbstractSqlWarnRepository {

    public MysqlWarnRepository(UserManager userManager) {
        super(userManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}

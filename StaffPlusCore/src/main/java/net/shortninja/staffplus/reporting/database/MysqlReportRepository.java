package net.shortninja.staffplus.reporting.database;

import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlReportRepository extends AbstractSqlReportRepository{

    public MysqlReportRepository(UserManager userManager) {
        super(userManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}

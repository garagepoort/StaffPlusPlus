package net.shortninja.staffplus.domain.staff.altaccountdetect.database.ipcheck;

import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlPlayerIpRepository extends AbstractSqlPlayerIpRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }

}

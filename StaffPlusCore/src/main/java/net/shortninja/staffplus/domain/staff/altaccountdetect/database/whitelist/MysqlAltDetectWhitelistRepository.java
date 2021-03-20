package net.shortninja.staffplus.domain.staff.altaccountdetect.database.whitelist;

import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlAltDetectWhitelistRepository extends AbstractSqlAltDetectWhitelistRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }

}

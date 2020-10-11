package net.shortninja.staffplus.staff.altaccountdetect.database.whitelist;

import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteAltDetectWhitelistRepository extends AbstractSqlAltDetectWhitelistRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

}

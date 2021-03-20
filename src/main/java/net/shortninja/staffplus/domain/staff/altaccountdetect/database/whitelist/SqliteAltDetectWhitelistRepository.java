package net.shortninja.staffplus.domain.staff.altaccountdetect.database.whitelist;

import net.shortninja.staffplus.application.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteAltDetectWhitelistRepository extends AbstractSqlAltDetectWhitelistRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

}

package net.shortninja.staffplus.domain.staff.altaccountdetect.database.ipcheck;

import net.shortninja.staffplus.application.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlitePlayerIpRepository extends AbstractSqlPlayerIpRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

}

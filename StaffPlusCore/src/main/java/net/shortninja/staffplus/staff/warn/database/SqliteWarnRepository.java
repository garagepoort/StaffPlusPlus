package net.shortninja.staffplus.staff.warn.database;

import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteWarnRepository extends AbstractSqlWarnRepository {

    public SqliteWarnRepository(UserManager userManager) {
        super(userManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }
}

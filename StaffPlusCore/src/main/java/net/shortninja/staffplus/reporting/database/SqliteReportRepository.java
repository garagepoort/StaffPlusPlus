package net.shortninja.staffplus.reporting.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteReportRepository extends AbstractSqlReportRepository {

    public SqliteReportRepository(PlayerManager playerManager) {
        super(playerManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }
}

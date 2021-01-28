package net.shortninja.staffplus.staff.warn.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteWarnRepository extends AbstractSqlWarnRepository {

    public SqliteWarnRepository(PlayerManager playerManager, Options options) {
        super(playerManager, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }
}

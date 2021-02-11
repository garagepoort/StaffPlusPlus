package net.shortninja.staffplus.staff.warn.warnings.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteWarnRepository extends AbstractSqlWarnRepository {

    public SqliteWarnRepository(PlayerManager playerManager, AppealRepository appealRepository, Options options) {
        super(playerManager, appealRepository, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }
}

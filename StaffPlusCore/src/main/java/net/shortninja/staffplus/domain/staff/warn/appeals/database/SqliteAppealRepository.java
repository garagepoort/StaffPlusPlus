package net.shortninja.staffplus.domain.staff.warn.appeals.database;

import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.application.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteAppealRepository extends AbstractSqlAppealRepository {

    public SqliteAppealRepository(PlayerManager playerManager) {
        super(playerManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }
}

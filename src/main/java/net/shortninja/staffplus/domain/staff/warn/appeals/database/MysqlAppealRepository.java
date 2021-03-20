package net.shortninja.staffplus.domain.staff.warn.appeals.database;

import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlAppealRepository extends AbstractSqlAppealRepository {

    public MysqlAppealRepository(PlayerManager playerManager) {
        super(playerManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}

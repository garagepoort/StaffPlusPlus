package net.shortninja.staffplus.staff.warn.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlWarnRepository extends AbstractSqlWarnRepository {

    public MysqlWarnRepository(PlayerManager playerManager, Options options) {
        super(playerManager, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}

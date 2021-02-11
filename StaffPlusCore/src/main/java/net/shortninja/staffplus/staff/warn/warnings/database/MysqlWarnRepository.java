package net.shortninja.staffplus.staff.warn.warnings.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlWarnRepository extends AbstractSqlWarnRepository {

    public MysqlWarnRepository(PlayerManager playerManager, AppealRepository appealRepository, Options options) {
        super(playerManager, appealRepository, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}

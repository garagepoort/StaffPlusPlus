package net.shortninja.staffplus.staff.reporting.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlReportRepository extends AbstractSqlReportRepository{

    public MysqlReportRepository(PlayerManager playerManager) {
        super(playerManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}

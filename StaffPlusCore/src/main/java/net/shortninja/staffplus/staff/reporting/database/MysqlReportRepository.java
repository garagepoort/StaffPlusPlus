package net.shortninja.staffplus.staff.reporting.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.*;

public class MysqlReportRepository extends AbstractSqlReportRepository {

    public MysqlReportRepository(PlayerManager playerManager, Options options) {
        super(playerManager, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }

    @Override
    public int addReport(Report report) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_reports(Reason, Reporter_UUID, Player_UUID, status, timestamp, server_name) " +
                 "VALUES(?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, report.getReason());
            insert.setString(2, report.getReporterUuid().toString());
            insert.setString(3, report.getCulpritUuid() == null ? null : report.getCulpritUuid().toString());
            insert.setString(4, report.getReportStatus().toString());
            insert.setLong(5, report.getTimestamp().toInstant().toEpochMilli());
            insert.setString(6, options.serverName);
            insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

            return generatedKey;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

package net.shortninja.staffplus.domain.staff.reporting.database;

import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.location.LocationRepository;
import net.shortninja.staffplus.domain.staff.reporting.Report;
import net.shortninja.staffplus.application.database.migrations.sqlite.SqlLiteConnection;

import java.sql.*;

public class SqliteReportRepository extends AbstractSqlReportRepository {

    private LocationRepository locationRepository;

    public SqliteReportRepository(PlayerManager playerManager, Options options, LocationRepository locationRepository) {
        super(playerManager, options);
        this.locationRepository = locationRepository;
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

    @Override
    public int addReport(Report report) {
        int locationId = locationRepository.addLocation(report.getLocation().get());
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_reports(Reason, Reporter_UUID, Player_UUID, status, timestamp, deleted, server_name, location_id, type) " +
                 "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setString(1, report.getReason());
            insert.setString(2, report.getReporterUuid().toString());
            insert.setString(3, report.getCulpritUuid() == null ? null : report.getCulpritUuid().toString());
            insert.setString(4, report.getReportStatus().toString());
            insert.setLong(5, report.getCreationDate().toInstant().toEpochMilli());
            insert.setBoolean(6, false);
            insert.setString(7, options.serverName);
            insert.setInt(8, locationId);
            if (report.getReportType().isPresent()) {
                insert.setString(9, report.getReportType().get());
            } else {
                insert.setNull(9, Types.VARCHAR);
            }
            insert.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }
            connection.commit(); // Commits transaction.

            return generatedKey;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

package net.shortninja.staffplus.core.domain.staff.reporting.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.location.LocationRepository;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;

import java.sql.*;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlReportRepository extends AbstractSqlReportRepository {

    private final LocationRepository locationRepository;

    public MysqlReportRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options, LocationRepository locationRepository) {
        super(playerManager, sqlConnectionProvider, options);
        this.locationRepository = locationRepository;
    }

    @Override
    public int addReport(Report report) {
        int locationId = locationRepository.addLocation(report.getLocation().get());
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_reports(Reason, Reporter_UUID, Player_UUID, status, timestamp, server_name, location_id, type) " +
                 "VALUES(?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, report.getReason());
            insert.setString(2, report.getReporterUuid().toString());
            insert.setString(3, report.getCulpritUuid() == null ? null : report.getCulpritUuid().toString());
            insert.setString(4, report.getReportStatus().toString());
            insert.setLong(5, report.getCreationDate().toInstant().toEpochMilli());
            insert.setString(6, options.serverName);
            insert.setInt(7, locationId);
            if (report.getReportType().isPresent()) {
                insert.setString(8, report.getReportType().get());
            } else {
                insert.setNull(8, Types.VARCHAR);
            }
            insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}

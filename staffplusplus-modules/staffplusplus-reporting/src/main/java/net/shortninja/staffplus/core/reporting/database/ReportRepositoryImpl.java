package net.shortninja.staffplus.core.reporting.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.location.LocationRepository;
import net.shortninja.staffplus.core.domain.location.SppLocation;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.reporting.Report;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.reports.ReportFilters;
import net.shortninja.staffplusplus.reports.ReportStatus;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.insertFilterValues;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.mapFilters;

@IocBean
public class ReportRepositoryImpl implements ReportRepository {

    private final PlayerManager playerManager;
    private final Options options;
    private final ServerSyncConfig reportSyncServers;
    private final LocationRepository locationRepository;
    private final QueryBuilderFactory query;

    public ReportRepositoryImpl(PlayerManager playerManager,
                                Options options,
                                LocationRepository locationRepository, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.options = options;
        this.reportSyncServers = options.serverSyncConfiguration.reportSyncServers;
        this.locationRepository = locationRepository;
        this.query = query;
    }

    @Override
    public int addReport(Report report) {
        Integer locationId = report.getLocation().isPresent() ? locationRepository.addLocation(report.getLocation().get()) : null;
        return query.create().insertQuery("INSERT INTO sp_reports(Reason, Reporter_UUID, reporter_name, Player_UUID, player_name, status, timestamp, server_name, location_id, type, deleted) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
            (insert) -> {
                insert.setString(1, report.getReason());
                insert.setString(2, report.getReporterUuid().toString());
                insert.setString(3, report.getReporterName());
                insert.setString(4, report.getCulpritUuid() == null ? null : report.getCulpritUuid().toString());
                insert.setString(5, report.getCulpritName());
                insert.setString(6, report.getReportStatus().toString());
                insert.setLong(7, report.getCreationDate().toInstant().toEpochMilli());
                insert.setString(8, options.serverName);
                if (locationId != null) {
                    insert.setInt(9, locationId);
                } else {
                    insert.setNull(9, Types.INTEGER);
                }

                if (report.getReportType().isPresent()) {
                    insert.setString(10, report.getReportType().get());
                } else {
                    insert.setNull(10, Types.VARCHAR);
                }
                // need to set this explicitly in older version because the deleted reports column has a wrong default "false" value.
                // This fix is easier than actually migrating the table.
                insert.setBoolean(11, false);
            });
    }

    @Override
    public List<Report> getReports(UUID uuid, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE Player_UUID = ? AND deleted=? "
                + getServerNameFilterWithAnd("sp_reports", reportSyncServers)
                + " ORDER BY timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, uuid.toString());
                ps.setBoolean(2, false);
                ps.setInt(3, offset);
                ps.setInt(4, amount);
            }, this::buildReport);
    }

    @Override
    public List<Report> findReports(ReportFilters reportFilters, int offset, int amount) {
        String filterQuery = mapFilters(reportFilters, true);
        String query = "SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE deleted=? " + filterQuery + getServerNameFilterWithAnd("sp_reports", reportSyncServers) + " ORDER BY timestamp DESC LIMIT ?,?";
        return this.query.create().find(query, (ps) -> {
            ps.setBoolean(1, false);
            int index = 2;
            index = insertFilterValues(reportFilters, ps, index);
            ps.setInt(index, offset);
            ps.setInt(index + 1, amount);
        }, this::buildReport);
    }

    @Override
    public List<Report> getReportsByOffender(UUID uuid) {
        return query.create().find("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE Player_UUID = ? AND deleted=? "
                + getServerNameFilterWithAnd("sp_reports", reportSyncServers)
                + " ORDER BY timestamp DESC",
            (ps) -> {
                ps.setString(1, uuid.toString());
                ps.setBoolean(2, false);
            }, this::buildReport);
    }

    @Override
    public List<Report> getUnresolvedReports(int offset, int amount) {
        return query.create().find("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE status = ? AND deleted=? " + getServerNameFilterWithAnd("sp_reports", reportSyncServers) + " ORDER BY timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, ReportStatus.OPEN.toString());
                ps.setBoolean(2, false);
                ps.setInt(3, offset);
                ps.setInt(4, amount);
            }, this::buildReport);
    }

    @Override
    public List<Report> getClosedReports(int offset, int amount) {
        return query.create().find("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE status IN (?,?,?) AND deleted=? "
                + getServerNameFilterWithAnd("sp_reports", reportSyncServers)
                + " ORDER BY timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, ReportStatus.REJECTED.toString());
                ps.setString(2, ReportStatus.RESOLVED.toString());
                ps.setString(3, ReportStatus.EXPIRED.toString());
                ps.setBoolean(4, false);
                ps.setInt(5, offset);
                ps.setInt(6, amount);
            }, this::buildReport);
    }

    @Override
    public long getReportCount(ReportFilters reportFilters) {
        String filterQuery = mapFilters(reportFilters, true);
        String query = "SELECT count(*) as count FROM sp_reports WHERE deleted=? " + filterQuery + getServerNameFilterWithAnd("sp_reports", reportSyncServers);

        return this.query.create().getOne(query, (ps) -> {
            ps.setBoolean(1, false);
            int index = 2;
            insertFilterValues(reportFilters, ps, index);
        }, (rs) -> rs.getLong("count"));
    }

    @Override
    public Optional<Report> findOpenReport(int reportId) {
        return query.create().findOne("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE sp_reports.id = ? AND status = ? AND deleted=?",
            (ps) -> {
                ps.setInt(1, reportId);
                ps.setString(2, ReportStatus.OPEN.toString());
                ps.setBoolean(3, false);
            }, this::buildReport);
    }

    @Override
    public Optional<Report> findReport(int reportId) {
        return query.create().findOne("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE sp_reports.id = ? AND deleted=?",
            (ps) -> {
                ps.setInt(1, reportId);
                ps.setBoolean(2, false);
            }, this::buildReport);
    }

    @Override
    public void updateReport(Report report) {
        query.create().updateQuery("UPDATE sp_reports set staff_name=?, staff_uuid=?, status=?, close_reason=? WHERE sp_reports.id=? AND deleted=?",
            (insert) -> {
                insert.setString(1, report.getStaffName());
                insert.setString(2, report.getStaffUuid() != null ? report.getStaffUuid().toString() : null);
                insert.setString(3, report.getReportStatus().toString());
                insert.setString(4, report.getCloseReason());
                insert.setInt(5, report.getId());
                insert.setBoolean(6, false);
            });
    }

    @Override
    public void markReportDeleted(Report report) {
        query.create().deleteQuery("UPDATE sp_reports set deleted=? WHERE id=?", (insert) -> {
            insert.setBoolean(1, true);
            insert.setInt(2, report.getId());
        });
    }

    @Override
    public List<Report> getAssignedReports(UUID staffUuid, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE staff_uuid = ? AND status = ? AND deleted=? " + getServerNameFilterWithAnd("sp_reports", reportSyncServers) + " ORDER BY timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, staffUuid.toString());
                ps.setString(2, ReportStatus.IN_PROGRESS.toString());
                ps.setBoolean(3, false);
                ps.setInt(4, offset);
                ps.setInt(5, amount);
            }, this::buildReport);
    }

    @Override
    public List<Report> getAssignedReports(int offset, int amount) {
        return query.create().find("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE status = ? AND deleted=? " + getServerNameFilterWithAnd("sp_reports", reportSyncServers) + " ORDER BY timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, ReportStatus.IN_PROGRESS.toString());
                ps.setBoolean(2, false);
                ps.setInt(3, offset);
                ps.setInt(4, amount);
            }, this::buildReport);
    }

    @Override
    public List<Report> getMyReports(UUID reporterUuid, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE Reporter_UUID = ? AND deleted=? "
                + getServerNameFilterWithAnd("sp_reports", reportSyncServers)
                + " ORDER BY timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, reporterUuid.toString());
                ps.setBoolean(2, false);
                ps.setInt(3, offset);
                ps.setInt(4, amount);
            }, this::buildReport);
    }

    @Override
    public List<Report> getMyReports(UUID reporterUuid) {
        return query.create().find("SELECT * FROM sp_reports LEFT OUTER JOIN sp_locations l on sp_reports.location_id = l.id WHERE Reporter_UUID = ? AND deleted=? " + getServerNameFilterWithAnd("sp_reports", reportSyncServers) + " ORDER BY timestamp",
            (ps) -> {
                ps.setString(1, reporterUuid.toString());
                ps.setBoolean(2, false);
            }, this::buildReport);
    }

    @Override
    public void removeReports(UUID playerUuid) {
        query.create().deleteQuery("DELETE FROM sp_reports WHERE Player_UUID = ? AND deleted=? " + getServerNameFilterWithAnd("sp_reports", reportSyncServers), (insert) -> {
            insert.setString(1, playerUuid.toString());
            insert.setBoolean(2, false);
        });
    }

    @Override
    public Map<UUID, Integer> getReportedCount() {
        return query.create().findMap("SELECT Player_UUID, count(*) as count FROM sp_reports WHERE Player_UUID is not null " + getServerNameFilterWithAnd(options.serverSyncConfiguration.kickSyncServers) + " GROUP BY Player_UUID ORDER BY count DESC",
            rs -> UUID.fromString(rs.getString("Player_UUID")),
            rs -> rs.getInt("count"));
    }

    private Report buildReport(ResultSet rs) throws SQLException {
        String playerUuid = rs.getString("Player_UUID");
        UUID reporterUUID = UUID.fromString(rs.getString("Reporter_UUID"));
        UUID staffUUID = rs.getString("staff_uuid") != null ? UUID.fromString(rs.getString("staff_uuid")) : null;

        String reporterName;
        if (reporterUUID.equals(CONSOLE_UUID)) {
            reporterName = "Console";
        } else {
            Optional<SppPlayer> reporter = playerManager.getOnOrOfflinePlayer(reporterUUID);
            reporterName = reporter.map(SppPlayer::getUsername).orElse("[Unknown player]");
        }

        UUID playerUUID = null;
        String culpritName = null;
        if (playerUuid != null) {
            playerUUID = UUID.fromString(playerUuid);
            Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUUID);
            culpritName = player.map(SppPlayer::getUsername).orElse("[Unknown player]");
        }

        int id = rs.getInt("ID");
        String serverName = rs.getString("server_name") == null ? "[Unknown]" : rs.getString("server_name");
        String type = rs.getString("type");
        Location location = null;
        SppLocation sppLocation = null;
        int locationId = rs.getInt(16);

        if (!rs.wasNull()) {
            double locationX = rs.getDouble(17);
            double locationY = rs.getDouble(18);
            double locationZ = rs.getDouble(19);
            String worldName = rs.getString(20);
            World locationWorld = Bukkit.getServer().getWorld(worldName);
            location = new Location(locationWorld, locationX, locationY, locationZ);
            sppLocation = new SppLocation(locationId, worldName, locationX, locationY, locationZ, serverName);
        }

        return new Report(playerUUID, culpritName, id,
            rs.getString("Reason"),
            reporterName,
            reporterUUID,
            rs.getLong("timestamp"),
            ReportStatus.valueOf(rs.getString("status")),
            rs.getString("staff_name"),
            staffUUID,
            rs.getString("close_reason"),
            serverName, location, sppLocation, type);
    }
}

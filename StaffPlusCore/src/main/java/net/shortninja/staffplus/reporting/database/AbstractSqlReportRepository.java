package net.shortninja.staffplus.reporting.database;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.reporting.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSqlReportRepository implements ReportRepository, IocContainer.Repository {

    private final PlayerManager playerManager;

    protected AbstractSqlReportRepository(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public void addReport(Report report) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_reports(Reason, Reporter_UUID, Player_UUID, status, timestamp) " +
                 "VALUES(?, ?, ?, ?, ?);")) {
            insert.setString(1, report.getReason());
            insert.setString(2, report.getReporterUuid().toString());
            insert.setString(3, report.getCulpritUuid() == null ? null : report.getCulpritUuid().toString());
            insert.setString(4, report.getReportStatus().toString());
            insert.setLong(5, report.getTimestamp().toInstant().toEpochMilli());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Report> getReports(UUID uuid, int offset, int amount) {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE Player_UUID = ? ORDER BY timestamp DESC LIMIT ?,?")) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }

    @Override
    public List<Report> getUnresolvedReports(int offset, int amount) {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE status = ?  ORDER BY timestamp DESC LIMIT ?,?")) {
            ps.setString(1, ReportStatus.OPEN.toString());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }

    @Override
    public List<Report> getClosedReports(int offset, int amount) {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE status IN (?,?,?)  ORDER BY timestamp DESC LIMIT ?,?")) {
            ps.setString(1, ReportStatus.REJECTED.toString());
            ps.setString(2, ReportStatus.RESOLVED.toString());
            ps.setString(3, ReportStatus.EXPIRED.toString());
            ps.setInt(4, offset);
            ps.setInt(5, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }

    @Override
    public Optional<Report> findOpenReport(int reportId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE id = ? AND status = ?")) {
            ps.setInt(1, reportId);
            ps.setString(2, ReportStatus.OPEN.toString());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Report> findReport(int reportId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE id = ?")) {
            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void updateReport(Report report) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_reports set staff_name=?, staff_uuid=?, status=?, close_reason=? WHERE id=?")) {
            insert.setString(1, report.getStaffName());
            insert.setString(2, report.getStaffUuid() != null ? report.getStaffUuid().toString() : null);
            insert.setString(3, report.getReportStatus().toString());
            insert.setString(4, report.getCloseReason());
            insert.setInt(5, report.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Report> getUnresolvedReports(UUID playerUuid, int offset, int amount) {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE Player_UUID = ? AND status = ? ORDER BY timestamp DESC LIMIT ?,?")) {
            ps.setString(1, playerUuid.toString());
            ps.setString(2, ReportStatus.OPEN.toString());
            ps.setInt(3, offset);
            ps.setInt(4, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }

    @Override
    public List<Report> getAssignedReports(UUID staffUuid, int offset, int amount) {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE staff_uuid = ? AND status = ? ORDER BY timestamp DESC LIMIT ?,?")) {
            ps.setString(1, staffUuid.toString());
            ps.setString(2, ReportStatus.IN_PROGRESS.toString());
            ps.setInt(3, offset);
            ps.setInt(4, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }

    @Override
    public void removeReports(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_reports WHERE Player_UUID = ?");) {
            insert.setString(1, playerUuid.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Report buildReport(ResultSet rs) throws SQLException {
        String player_uuid = rs.getString("Player_UUID");
        UUID reporterUUID = UUID.fromString(rs.getString("Reporter_UUID"));
        UUID staffUUID = rs.getString("staff_uuid") != null ? UUID.fromString(rs.getString("staff_uuid")) : null;

        String reporterName;
        if (reporterUUID.equals(StaffPlus.get().consoleUUID)) {
            reporterName = "Console";
        } else {
            Optional<SppPlayer> reporter = playerManager.getOnOrOfflinePlayer(reporterUUID);
            reporterName = reporter.map(SppPlayer::getUsername).orElse(null);
        }

        UUID playerUUID = null;
        String culpritName = null;
        if (player_uuid != null) {
            playerUUID = UUID.fromString(player_uuid);
            Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUUID);
            culpritName = player.map(SppPlayer::getUsername).orElse(null);
        }

        int id = rs.getInt("ID");
        return new Report(playerUUID, culpritName, id,
            rs.getString("Reason"),
            reporterName,
            reporterUUID,
            rs.getLong("timestamp"),
            ReportStatus.valueOf(rs.getString("status")),
            rs.getString("staff_name"),
            staffUUID,
            rs.getString("close_reason"));
    }

}

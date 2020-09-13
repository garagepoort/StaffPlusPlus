package net.shortninja.staffplus.reporting.database;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.player.ProvidedPlayer;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.reporting.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.IocContainer.getUserManager;

public abstract class AbstractSqlReportRepository implements ReportRepository {

    private final UserManager userManager = StaffPlus.get().getUserManager();

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public void addReport(Report report) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_reports(Reason, Reporter_UUID, Player_UUID, status, timestamp) " +
                     "VALUES(?, ?, ?, ?, ?);");) {
            insert.setString(1, report.getReason());
            insert.setString(2, report.getReporterUuid().toString());
            insert.setString(3, report.getCulpritUuid() == null ? null : report.getCulpritUuid().toString());
            insert.setString(4, report.getReportStatus().toString());
            insert.setLong(5, System.currentTimeMillis());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Report> getReports(UUID uuid) {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE Player_UUID = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public List<Report> getUnresolvedReports() {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE status = ?")) {
            ps.setString(1, ReportStatus.OPEN.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public List<Report> getClosedReports() {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE status IN (?,?,?)")) {
            ps.setString(1, ReportStatus.REJECTED.toString());
            ps.setString(2, ReportStatus.RESOLVED.toString());
            ps.setString(3, ReportStatus.EXPIRED.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                if(first){
                    return Optional.of(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                if(first){
                    return Optional.of(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void updateReport(Report report) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_reports set staff_name=?, staff_uuid=?, status=? WHERE id=?");) {
            insert.setString(1, report.getStaffName());
            insert.setString(2, report.getStaffUuid().toString());
            insert.setString(3, report.getReportStatus().toString());
            insert.setInt(4, report.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Report> getUnresolvedReports(UUID playerUuid) {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE Player_UUID = ? AND status = ?")) {
            ps.setString(1, playerUuid.toString());
            ps.setString(2, ReportStatus.OPEN.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public List<Report> getAssignedReports(UUID staffUuid) {
        List<Report> reports = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE staff_uuid = ? AND status = ? ORDER BY timestamp DESC")) {
            ps.setString(1, staffUuid.toString());
            ps.setString(2, ReportStatus.IN_PROGRESS.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(buildReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public void removeReports(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_reports WHERE UUID = ?");) {
            insert.setString(1, playerUuid.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
            Optional<ProvidedPlayer> reporter = getUserManager().getOnOrOfflinePlayer(reporterUUID);
            reporterName = reporter.map(ProvidedPlayer::getUsername).orElse(null);
        }

        UUID playerUUID = null;
        String culpritName = null;
        if(player_uuid != null) {
            playerUUID = UUID.fromString(player_uuid);
            Optional<ProvidedPlayer> player = getUserManager().getOnOrOfflinePlayer(playerUUID);
            culpritName = player.map(ProvidedPlayer::getUsername).orElse(null);
        }

        int id = rs.getInt("ID");
        return new Report(playerUUID, culpritName, id,
                rs.getString("Reason"),
                reporterName,
                reporterUUID,
                rs.getLong("timestamp"),
                ReportStatus.valueOf(rs.getString("status")),
                rs.getString("staff_name"),
                staffUUID);
    }

}

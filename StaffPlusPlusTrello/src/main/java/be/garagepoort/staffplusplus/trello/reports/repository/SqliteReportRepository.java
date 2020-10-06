package be.garagepoort.staffplusplus.trello.reports.repository;

import be.garagepoort.staffplusplus.trello.reports.Report;
import be.garagepoort.staffplusplus.trello.repository.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SqliteReportRepository implements ReportRepository {

    @Override
    public Optional<Report> findReportBySppId(int sppId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM spp_trello_reports WHERE spp_id = ?")) {
            ps.setInt(1, sppId);
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
    public void createReport(Report report) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO spp_trello_reports(spp_id, trello_id) " +
                 "VALUES(?, ?);")) {
            insert.setInt(1, report.getSppId());
            insert.setString(2, report.getTrelloId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

    private Report buildReport(ResultSet rs) throws SQLException {
        return new Report(
            rs.getInt("ID"),
            rs.getInt("spp_id"),
            rs.getString("trello_id"));
    }
}

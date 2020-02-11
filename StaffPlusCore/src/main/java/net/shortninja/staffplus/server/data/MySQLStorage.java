package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLStorage implements IStorage {

    private final MySQLConnection connection;

    public MySQLStorage(MySQLConnection connection) {
        this.connection = connection;
    }

    @Override
    public void onEnable() {
        if (connection.init())
            Bukkit.getLogger().info("Database created");
    }

    @Override
    public void onDisable() {
        connection.kill();
    }

    @Override
    public byte[] getPassword(Player player) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT Password FROM sp_playerdata WHERE Player_UUID=?;")) {
            ps.setString(1, player.getUniqueId().toString());

            try (ResultSet set = ps.executeQuery()) {
                return set.next() ? set.getBytes("Password") : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not open connection.", e);
        }
    }

    @Override
    public void setPassword(Player player, byte[] password) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO sp_playerdata (Player_UUID, Password) VALUES (?, ?) ON DUPLICATE KEY UPDATE Password=?;")) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setBytes(2, password);
            ps.setBytes(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not open connection.", e);
        }
    }

    @Override
    public short getGlassColor(User user) {
        try (Connection sql = MySQLConnection.getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT GlassColor FROM sp_playerdata WHERE Player_UUID=?")) {
            ps.setString(1, user.getUuid().toString());
            short data = 0;
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next())
                    data = rs.getShort("GlassColor");
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void setGlassColor(User user, short glassColor) {
        try (Connection sql = MySQLConnection.getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_playerdata(GlassColor, Player_UUID) " +
                     "VALUES(?, ?) ON DUPLICATE KEY UPDATE GlassColor=?;")) {
            insert.setInt(1, glassColor);
            insert.setString(2, user.getUuid().toString());
            insert.setInt(3, glassColor);
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<IReport> getReports(UUID uuid) {
        List<IReport> reports = new ArrayList<>();
        try (Connection sql = MySQLConnection.getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE Player_UUID = ?")) {
            ps.setString(1,uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID playerUUID = UUID.fromString(rs.getString("Player_UUID"));
                    UUID reporterUUID = UUID.fromString(rs.getString("Reporter_UUID"));
                    String reporterName = reporterUUID.equals(StaffPlus.get().consoleUUID) ? "Console" : Bukkit.getPlayer(reporterUUID).getDisplayName();
                    int id = rs.getInt("ID");
                    reports.add(new Report(playerUUID, Bukkit.getPlayer(playerUUID).getDisplayName(), id, rs.getString("Reason"), reporterName, reporterUUID, System.currentTimeMillis()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public List<IWarning> getWarnings(UUID uuid) {
        List<IWarning> warnings = new ArrayList<>();
        try (Connection sql = MySQLConnection.getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings WHERE Player_UUID = ?")
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID playerUUID = UUID.fromString(rs.getString("Player_UUID"));
                    UUID warnerUuid = UUID.fromString(rs.getString("Warner_UUID"));
                    String warnerName = warnerUuid.equals(StaffPlus.get().consoleUUID) ? "Console" : Bukkit.getPlayer(warnerUuid).getDisplayName();
                    int id = rs.getInt("ID");
                    //NPE \/
                    warnings.add(new Warning(playerUUID, Bukkit.getPlayer(playerUUID).getDisplayName(), id, rs.getString("Reason"), warnerName, warnerUuid, System.currentTimeMillis()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warnings;
    }

    @Override
    public void addReport(IReport report) {
        try (Connection sql = MySQLConnection.getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_reports(Reason, Reporter_UUID, Player_UUID) " +
                     "VALUES(?, ?, ?);");) {
            insert.setString(1, report.getReason());
            insert.setString(2, report.getReporterUuid().toString());
            insert.setString(3, report.getUuid().toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addWarning(IWarning warning) {
        try (Connection sql = MySQLConnection.getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_warnings(Reason, Warner_UUID, Player_UUID) " +
                     "VALUES(? ,?, ?);");) {
            insert.setString(1, warning.getReason());
            insert.setString(2, warning.getIssuerUuid().toString());
            insert.setString(3, warning.getUuid().toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeReport(User user) {

    }

    @Override
    public void removeWarning(UUID uuid) {
        try (Connection sql = MySQLConnection.getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_warnings WHERE Player_UUID = ?");) {
             insert.setString(1, uuid.toString());
             insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ticket getTicketByUUID(User user) {
        return null;
    }

    @Override
    public Ticket getTickById(int id) {
        return null;
    }

    @Override
    public void addTicket(Ticket ticket) {

    }

    @Override
    public void removeTicket(User user) {

    }
}

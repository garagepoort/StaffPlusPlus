package net.shortninja.staffplus.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;
import net.shortninja.staffplus.server.AlertCoordinator.AlertType;
import net.shortninja.staffplus.server.data.MySQLConnection;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User {
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UUID uuid;
    private String name;
    private short glassColor;
    private List<Report> reports = new ArrayList<>();
    private List<Warning> warnings = new ArrayList<>();
    private VanishType vanishType = VanishType.NONE;
    private List<String> playerNotes = new ArrayList<String>();
    private AbstractGui currentGui = null;
    private AbstractGui.AbstractAction queuedAction = null;
    private Map<AlertType, Boolean> alertOptions = new HashMap<AlertType, Boolean>();
    private boolean isOnline = true;
    private boolean isChatting = false;
    private boolean isFrozen = false;

    public User(UUID uuid, String name, short glassColor, List<Report> reports, List<Warning> warnings, List<String> playerNotes, Map<AlertType, Boolean> alertOptions) {
        this.uuid = uuid;
        this.name = name;
        this.glassColor = glassColor;
        this.reports = reports;
        this.warnings = warnings;
        this.playerNotes = playerNotes;
        this.alertOptions = alertOptions;
    }

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.glassColor = options.glassColor;
        this.name = name;

        for (AlertType alertType : AlertType.values()) {
            setAlertOption(alertType, true);
        }
    }

    /**
     * This method can return a null player if the user is not online, so be sure
     * to check!
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(name);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public short getGlassColor() {
        if (options.storageType.equalsIgnoreCase("flatefile"))
            return glassColor;
        else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement ps = sql.prepareStatement("SELECT GlassColor FROM sp_playerdata WHERE Player_UUID=?")) {
                ps.setString(1, uuid.toString());
                short data;
                try (ResultSet rs = ps.executeQuery()) {
                    data = rs.getShort("GlassColor");
                }
                return data;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return (short) 0;
    }

    public void setGlassColor(short glassColor) {
        if (options.storageType.equalsIgnoreCase("flatfile"))
            this.glassColor = glassColor;
        else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_playerdata(GlassColor, Player_UUID) " +
                         "VALUES(?, ?) ON DUPLICATE KEY UPDATE GlassColor=?;")) {
                insert.setInt(1, glassColor);
                insert.setString(2, getUuid().toString());
                insert.setInt(3, glassColor);
                insert.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public List<Report> getReports() {
        if (options.storageType.equalsIgnoreCase("flatfile"))
            return reports;
        else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement ps = sql.prepareStatement("SELECT ID FROM sp_reports WHERE Player_UUID=?");) {
                ps.setString(1, uuid.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next())
                        reports.add(new Report(UUID.fromString(rs.getString("Player_UUID")), Bukkit.getPlayer(UUID.fromString(rs.getString("Player_UUID"))).getDisplayName(),
                                rs.getInt("ID"), rs.getString("Reason"), Bukkit.getPlayer(UUID.fromString("Reporter_UUID")).getDisplayName(), UUID.fromString(rs.getString("Reporter_UUID")), System.currentTimeMillis()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reports;
    }

    public List<Warning> getWarnings() {
        if (options.storageType.equalsIgnoreCase("flatfile"))
            return warnings;
        else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement ps = sql.prepareStatement("SELECT ID FROM sp_warnings WHERE Player_UUID=?");
            ) {
                ps.setString(1, uuid.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next())
                        warnings.add(new Warning(UUID.fromString(rs.getString("Player_UUID")), Bukkit.getPlayer(UUID.fromString(rs.getString("Player_UUID"))).getDisplayName(),
                                rs.getInt("ID"), rs.getString("Reason"), Bukkit.getPlayer(UUID.fromString("Warner_UUID")).getDisplayName(), UUID.fromString(rs.getString("Warner_UUID")), System.currentTimeMillis()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return warnings;
    }

    public List<String> getPlayerNotes() {
        return playerNotes;
    }

    public VanishType getVanishType() {
        return vanishType;
    }

    /**
     * This method should NOT be used if you want to update the user's vanish
     * type! Use the vanish handler!
     */
    public void setVanishType(VanishType vanishType) {
        this.vanishType = vanishType;
    }

    public AbstractGui getCurrentGui() {
        return currentGui;
    }

    public void setCurrentGui(AbstractGui currentGui) {
        this.currentGui = currentGui;
    }

    public AbstractGui.AbstractAction getQueuedAction() {
        return queuedAction;
    }

    public void setQueuedAction(AbstractGui.AbstractAction queuedAction) {
        this.queuedAction = queuedAction;
    }

    public boolean shouldNotify(AlertType alertType) {
        return alertOptions.get(alertType) == null ? false : alertOptions.get(alertType);
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isChatting() {
        return isChatting;
    }

    public void setChatting(boolean isChatting) {
        this.isChatting = isChatting;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    public void setAlertOption(AlertType alertType, boolean isEnabled) {
        if (alertOptions.containsKey(alertType)) {
            alertOptions.replace(alertType, isEnabled);
        } else {
            alertOptions.put(alertType, isEnabled);
        }
    }

    public void addReport(Report report) {
        if (options.storageType.equalsIgnoreCase("flatfile"))
            reports.add(report);
        else if (options.storageType.equalsIgnoreCase("mysql")) {
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
    }

    public void removeReport(String uuid) {
        reports.remove(uuid);
    }

    public void addWarning(Warning warning) {
        if (options.storageType.equalsIgnoreCase("flatfile"))
            warnings.add(warning);
        else if (options.storageType.equalsIgnoreCase("mysql")) {
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
    }

    public void removeWarning(UUID uuid) {
        warnings.remove(uuid);
    }

    public void addPlayerNote(String note) {
        playerNotes.add(note);
    }
}
package net.shortninja.staffplus.player;

import net.shortninja.staffplus.StaffPlus;
<<<<<<< HEAD
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.server.data.MySQLConnection;
=======
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.*;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
=======
import java.lang.reflect.Field;
import java.lang.reflect.Method;
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
import java.util.*;

public class User implements IUser {
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UUID uuid;
    private String name;
    protected short glassColor;
    private List<IReport> reports = new ArrayList<>();
    private List<IWarning> warnings = new ArrayList<>();
    private VanishType vanishType = VanishType.NONE;
    private List<String> playerNotes = new ArrayList<String>();
    private IGui currentGui = null;
    private IAction queuedAction = null;
    private Map<AlertType, Boolean> alertOptions = new HashMap<AlertType, Boolean>();
    private boolean isOnline = true;
    private boolean isChatting = false;
    private boolean isFrozen = false;

<<<<<<< HEAD
=======
    private static Class<?> craftPlayerClass;
    private static Class<?> entityPlayerClass;
    private static Class<?> playerConnectionClass;
    private static Method getHandleMethod;
    private static Field playerConnectionField;
    private static Field pingField;

    static {
        try {
            final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
//            final String version = StaffPlus.get().versionProtocol.getVersion();

            craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            entityPlayerClass = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
            playerConnectionClass = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
            getHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
            playerConnectionField = entityPlayerClass.getDeclaredField("playerConnection");
            pingField = playerConnectionClass.getDeclaredField("ping");
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
    public User(UUID uuid, String name, short glassColor, List<IReport> reports, List<IWarning> warnings, List<String> playerNotes, Map<AlertType, Boolean> alertOptions) {
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
<<<<<<< HEAD
=======
     *
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
     * @return
     */
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }


<<<<<<< HEAD
    private short getColorColor(){
=======
    private short getColorColor() {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
        return glassColor;
    }

    public short getGlassColor() {
<<<<<<< HEAD
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

    public List<IReport> getReports() {
        if (options.storageType.equalsIgnoreCase("flatfile"))
            return reports;
        else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_reports WHERE Player_UUID = ?")) {
                ps.setString(1, uuid.toString());
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
        }
        return reports;
    }

    public List<IWarning> getWarnings() {
        if (options.storageType.equalsIgnoreCase("flatfile"))
            return warnings;
        else if (options.storageType.equalsIgnoreCase("mysql")) {
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
                        warnings.add(new Warning(playerUUID, Bukkit.getPlayer(playerUUID).getDisplayName(), id, rs.getString("Reason"), warnerName, warnerUuid, System.currentTimeMillis()));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return warnings;
=======
        return StaffPlus.get().storage.getGlassColor(this);
    }

    public void setGlassColor(short glassColor) {
        StaffPlus.get().storage.setGlassColor(this, glassColor);
    }

    public List<IReport> getReports() {
        return StaffPlus.get().storage.getReports(getUuid());
    }

    public List<IWarning> getWarnings() {
        return StaffPlus.get().storage.getWarnings(getUuid());
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
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

    public Optional<IGui> getCurrentGui() {
        return Optional.ofNullable(currentGui);
    }

    public void setCurrentGui(IGui currentGui) {
        this.currentGui = currentGui;
    }

    public IAction getQueuedAction() {
        return queuedAction;
    }

    public void setQueuedAction(IAction queuedAction) {
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

<<<<<<< HEAD
=======
    public static int getPing(Player player) {
        try {
            Object entityPlayer = getHandleMethod.invoke(player);
            Object playerConnection = playerConnectionField.get(entityPlayer);

            return (int) pingField.get(playerConnection);
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().warning(e.getMessage());
            return -1;
        }
    }

>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
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

    public void addReport(IReport report) {
<<<<<<< HEAD
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
=======
        StaffPlus.get().storage.addReport(report);
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
    }

    public void removeReport(String uuid) {
        reports.remove(uuid);
    }

    public void addWarning(IWarning warning) {
<<<<<<< HEAD
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
=======
        StaffPlus.get().storage.addWarning(warning);
    }

    public void removeWarning(UUID uuid) {
        StaffPlus.get().storage.removeWarning(uuid);
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
    }

    public void addPlayerNote(String note) {
        playerNotes.add(note);
    }
}
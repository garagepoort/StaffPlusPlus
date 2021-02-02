package net.shortninja.staffplus.staff.warn.database;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.Constants;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.Warning;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractSqlWarnRepository implements WarnRepository {

    private final PlayerManager playerManager;
    protected final Options options;
    private final String serverNameFilter;

    protected AbstractSqlWarnRepository(PlayerManager playerManager, Options options) {
        this.playerManager = playerManager;
        this.options = options;
        serverNameFilter = !options.serverSyncConfiguration.isWarningSyncEnabled() ? "AND (server_name is null OR server_name='" + options.serverName + "')" : "";
    }

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public int getTotalScore(UUID uuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ifnull(sum(score), 0) sum FROM sp_warnings WHERE Player_UUID = ? " + serverNameFilter)
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("sum");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Warning> getWarnings(UUID uuid) {
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings WHERE Player_UUID = ? " + serverNameFilter)
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return warnings;
    }

    @Override
    public List<Warning> getWarnings(UUID uuid, int offset, int amount) {
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings WHERE Player_UUID = ? " + serverNameFilter + " ORDER BY timestamp DESC LIMIT ?,?")
        ) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return warnings;
    }

    @Override
    public List<Warning> getWarnings() {
        String sqlStatement = options.serverSyncConfiguration.isWarningSyncEnabled() ? "SELECT * FROM sp_warnings WHERE (server_name is null OR server_name='" + options.serverName + "')" : "SELECT * FROM sp_warnings";
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(sqlStatement)
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return warnings;
    }

    @Override
    public void addWarning(Warning warning) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_warnings(Reason, Warner_UUID, Player_UUID, score, severity, timestamp, server_name) " +
                 "VALUES(? ,?, ?, ?, ?, ?, ?);")) {
            insert.setString(1, warning.getReason());
            insert.setString(2, warning.getIssuerUuid().toString());
            insert.setString(3, warning.getUuid().toString());
            insert.setInt(4, warning.getScore());
            insert.setString(5, warning.getSeverity());
            insert.setLong(6, warning.getTime());
            insert.setString(7, options.serverName);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeWarnings(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_warnings WHERE Player_UUID = ? " + serverNameFilter);) {
            insert.setString(1, playerUuid.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeWarning(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_warnings WHERE ID = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void markWarningsRead(UUID uniqueId) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_warnings set is_read=true WHERE Player_UUID=? " + serverNameFilter + ";")) {
            insert.setString(1, uniqueId.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        Map<UUID, Integer> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT Player_UUID, count(*) as count FROM sp_warnings " + Constants.getServerNameFilterWithWhere(options.serverSyncConfiguration.isWarningSyncEnabled()) + " GROUP BY Player_UUID ORDER BY count DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count.put(UUID.fromString(rs.getString("Player_UUID")), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    private Warning buildWarning(ResultSet rs) throws SQLException {
        UUID playerUUID = UUID.fromString(rs.getString("Player_UUID"));
        UUID warnerUuid = UUID.fromString(rs.getString("Warner_UUID"));
        int score = rs.getInt("score");
        String severity = rs.getString("severity") == null ? "No Severity" : rs.getString("severity");

        Optional<SppPlayer> warner = playerManager.getOnOrOfflinePlayer(warnerUuid);
        String warnerName = warnerUuid.equals(StaffPlus.get().consoleUUID) ? "Console" : warner.map(SppPlayer::getUsername).orElse("Unknown user");
        int id = rs.getInt("ID");
        boolean read = rs.getBoolean("is_read");

        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUUID);
        String name = player.map(SppPlayer::getUsername).orElse("Unknown user");
        return new Warning(playerUUID, name, id, rs.getString("Reason"), warnerName, warnerUuid, rs.getLong("timestamp"), score, severity, read);
    }

}

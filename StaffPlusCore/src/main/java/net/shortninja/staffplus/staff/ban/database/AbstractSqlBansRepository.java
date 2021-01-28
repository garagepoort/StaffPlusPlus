package net.shortninja.staffplus.staff.ban.database;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.ban.Ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSqlBansRepository implements BansRepository {

    private final PlayerManager playerManager;
    protected final Options options;
    private final String serverNameFilter;

    protected AbstractSqlBansRepository(PlayerManager playerManager, Options options) {
        this.playerManager = playerManager;
        this.options = options;
        serverNameFilter = !options.serverSyncConfiguration.isBanSyncEnabled() ? "AND (server_name is null OR server_name='" + options.serverName + "')" : "";
    }

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public List<Ban> getActiveBans(int offset, int amount) {
        List<Ban> bans = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + serverNameFilter + " ORDER BY creation_timestamp DESC LIMIT ?,?")) {
            ps.setLong(1, System.currentTimeMillis());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bans.add(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bans;
    }

    @Override
    public Optional<Ban> findActiveBan(int banId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE id = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + serverNameFilter)) {
            ps.setInt(1, banId);
            ps.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ban> findActiveBan(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE player_uuid = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + serverNameFilter)) {
            ps.setString(1, playerUuid.toString());
            ps.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Ban> getBansForPlayer(UUID playerUuid) {
        List<Ban> bans = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE player_uuid = ? " + serverNameFilter + " ORDER BY creation_timestamp DESC")) {
            ps.setString(1, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bans.add(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bans;
    }

    @Override
    public void update(Ban ban) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_banned_players set unbanned_by_uuid=?, unban_reason=?, end_timestamp=? WHERE ID=?")) {
            insert.setString(1, ban.getUnbannedByUuid().toString());
            insert.setString(2, ban.getUnbanReason());
            insert.setLong(3, System.currentTimeMillis());
            insert.setInt(4, ban.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Ban buildBan(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        UUID issuerUuid = UUID.fromString(rs.getString("issuer_uuid"));
        UUID unbannedByUUID = rs.getString("unbanned_by_uuid") != null ? UUID.fromString(rs.getString("unbanned_by_uuid")) : null;

        String playerName = getPlayerName(playerUuid);
        String issuerName = getPlayerName(issuerUuid);

        String unbannedByName = null;
        if (unbannedByUUID != null) {
            unbannedByName = getPlayerName(unbannedByUUID);
        }

        int id = rs.getInt("ID");
        Long endTimestamp = rs.getLong("end_timestamp");
        endTimestamp = rs.wasNull() ? null : endTimestamp;

        return new Ban(
            id,
            rs.getString("reason"),
            rs.getLong("creation_timestamp"),
            endTimestamp,
            playerName,
            playerUuid,
            issuerName,
            issuerUuid,
            unbannedByName,
            unbannedByUUID,
            rs.getString("unban_reason"));
    }

    private String getPlayerName(UUID uuid) {
        String issuerName;
        if (uuid.equals(StaffPlus.get().consoleUUID)) {
            issuerName = "Console";
        } else {
            Optional<SppPlayer> issuer = playerManager.getOnOrOfflinePlayer(uuid);
            issuerName = issuer.map(SppPlayer::getUsername).orElse(null);
        }
        return issuerName;
    }

}

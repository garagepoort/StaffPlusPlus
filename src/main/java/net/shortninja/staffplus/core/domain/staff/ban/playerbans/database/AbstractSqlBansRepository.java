package net.shortninja.staffplus.core.domain.staff.ban.playerbans.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.database.SqlRepository;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithWhere;

public abstract class AbstractSqlBansRepository extends SqlRepository implements BansRepository {

    private final PlayerManager playerManager;
    private final SqlConnectionProvider sqlConnectionProvider;
    protected final Options options;

    public AbstractSqlBansRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.playerManager = playerManager;
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.options = options;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public List<Ban> getActiveBans(int offset, int amount) {
        List<Ban> bans = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?")) {
            ps.setLong(1, System.currentTimeMillis());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bans.add(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return bans;
    }

    @Override
    public Optional<Ban> findActiveBan(int banId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE id = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers))) {
            ps.setInt(1, banId);
            ps.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ban> findActiveBan(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE player_uuid = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers))) {
            ps.setString(1, playerUuid.toString());
            ps.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Ban> getBansForPlayer(UUID playerUuid) {
        List<Ban> bans = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE player_uuid = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " ORDER BY creation_timestamp DESC")) {
            ps.setString(1, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bans.add(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return bans;
    }
    @Override
    public List<Ban> getBansForPlayerPaged(UUID playerUuid, int offset, int amount) {
        List<Ban> bans = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE player_uuid = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?")) {
            ps.setString(1, playerUuid.toString());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bans.add(buildBan(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return bans;
    }
    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        Map<UUID, Integer> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid, count(*) as count FROM sp_banned_players " + getServerNameFilterWithWhere(options.serverSyncConfiguration.banSyncServers) + " GROUP BY player_uuid ORDER BY count DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count.put(UUID.fromString(rs.getString("player_uuid")), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return count;
    }

    @Override
    public long getTotalCount() {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT count(*) as count FROM sp_banned_players " + getServerNameFilterWithWhere(options.serverSyncConfiguration.banSyncServers))) {
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return rs.getLong("count");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return 0;
    }

    @Override
    public void setBanDuration(int banId, long newDuration) {
        try (Connection sql = getConnection();
             PreparedStatement update = sql.prepareStatement("UPDATE sp_banned_players set end_timestamp=? WHERE id=? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers))) {
            update.setLong(1, newDuration);
            update.setInt(2, banId);
            update.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public long getActiveCount() {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT count(*) as count FROM sp_banned_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers))) {
            ps.setLong(1, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return rs.getLong("count");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return 0;
    }

    @Override
    public Map<UUID, Long> getBanDurationByPlayer() {
        Map<UUID, Long> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid, sum(end_timestamp - creation_timestamp) as count FROM sp_banned_players WHERE end_timestamp is not null " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " GROUP BY player_uuid ORDER BY count DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count.put(UUID.fromString(rs.getString("player_uuid")), rs.getLong("count"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return count;
    }

    @Override
    public List<UUID> getAllPermanentBannedPlayers() {
        List<UUID> result = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid FROM sp_banned_players WHERE end_timestamp IS NULL " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " GROUP BY player_uuid")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(UUID.fromString(rs.getString("player_uuid")));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return result;
    }

    @Override
    public void update(Ban ban) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_banned_players set unbanned_by_uuid=?, unban_reason=?, end_timestamp=?, silent_unban=? WHERE ID=?")) {
            insert.setString(1, ban.getUnbannedByUuid().toString());
            insert.setString(2, ban.getUnbanReason());
            insert.setLong(3, System.currentTimeMillis());
            insert.setBoolean(4, ban.isSilentUnban());
            insert.setInt(5, ban.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private Ban buildBan(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        UUID issuerUuid = UUID.fromString(rs.getString("issuer_uuid"));
        UUID unbannedByUUID = rs.getString("unbanned_by_uuid") != null ? UUID.fromString(rs.getString("unbanned_by_uuid")) : null;

        String playerName = rs.getString("player_name");
        String issuerName = rs.getString("issuer_name");
        boolean silentBan = rs.getBoolean("silent_ban");
        boolean silentUnban = rs.getBoolean("silent_unban");

        String unbannedByName = null;
        if (unbannedByUUID != null) {
            unbannedByName = getPlayerName(unbannedByUUID);
        }

        int id = rs.getInt("ID");
        Long endTimestamp = rs.getLong("end_timestamp");
        endTimestamp = rs.wasNull() ? null : endTimestamp;
        String serverName = rs.getString("server_name") == null ? "[Unknown]" : rs.getString("server_name");

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
            rs.getString("unban_reason"),
            serverName, silentBan, silentUnban,
            rs.getString("template"));
    }

    private String getPlayerName(UUID uuid) {
        String issuerName;
        if (uuid.equals(CONSOLE_UUID)) {
            issuerName = "Console";
        } else {
            Optional<SppPlayer> issuer = playerManager.getOnOrOfflinePlayer(uuid);
            issuerName = issuer.map(SppPlayer::getUsername).orElse("[Unknown player]");
        }
        return issuerName;
    }

}

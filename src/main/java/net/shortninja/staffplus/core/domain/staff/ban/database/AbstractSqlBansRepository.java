package net.shortninja.staffplus.core.domain.staff.ban.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.ban.Ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;

public abstract class AbstractSqlBansRepository implements BansRepository {

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
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.isBanSyncEnabled()) + " ORDER BY creation_timestamp DESC LIMIT ?,?")) {
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
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE id = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.isBanSyncEnabled()))) {
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
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE player_uuid = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.isBanSyncEnabled()))) {
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
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_players WHERE player_uuid = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.isBanSyncEnabled()) + " ORDER BY creation_timestamp DESC")) {
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
    public Map<UUID, Integer> getCountByPlayer() {
        Map<UUID, Integer> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid, count(*) as count FROM sp_banned_players " + Constants.getServerNameFilterWithWhere(options.serverSyncConfiguration.isBanSyncEnabled()) + " GROUP BY player_uuid ORDER BY count DESC")) {
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
    public Map<UUID, Long> getBanDurationByPlayer() {
        Map<UUID, Long> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid, sum(end_timestamp - creation_timestamp) as count FROM sp_banned_players WHERE end_timestamp is not null " + Constants.getServerNameFilterWithAnd(options.serverSyncConfiguration.isWarningSyncEnabled()) + " GROUP BY player_uuid ORDER BY count DESC")) {
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
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid FROM sp_banned_players WHERE end_timestamp IS NULL " + Constants.getServerNameFilterWithAnd(options.serverSyncConfiguration.isWarningSyncEnabled()) + " GROUP BY player_uuid")) {
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
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_banned_players set unbanned_by_uuid=?, unban_reason=?, end_timestamp=? WHERE ID=?")) {
            insert.setString(1, ban.getUnbannedByUuid().toString());
            insert.setString(2, ban.getUnbanReason());
            insert.setLong(3, System.currentTimeMillis());
            insert.setInt(4, ban.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
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
            serverName);
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

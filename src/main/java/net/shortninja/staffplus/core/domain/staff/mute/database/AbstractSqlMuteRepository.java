package net.shortninja.staffplus.core.domain.staff.mute.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.appeals.database.AppealRepository;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.appeals.AppealableType;
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
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithWhere;

public abstract class AbstractSqlMuteRepository implements MuteRepository {

    private final PlayerManager playerManager;
    protected final Options options;
    protected final SqlConnectionProvider sqlConnectionProvider;
    private final ServerSyncConfig muteSyncServers;
    private final AppealRepository appealRepository;

    public AbstractSqlMuteRepository(PlayerManager playerManager, Options options, SqlConnectionProvider sqlConnectionProvider, AppealRepository appealRepository) {
        this.playerManager = playerManager;
        this.options = options;
        this.sqlConnectionProvider = sqlConnectionProvider;
        muteSyncServers = options.serverSyncConfiguration.muteSyncServers;
        this.appealRepository = appealRepository;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public List<Mute> getActiveMutes(int offset, int amount) {
        List<Mute> mutes = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?")) {
            ps.setLong(1, System.currentTimeMillis());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mutes.add(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return mutes;
    }

    @Override
    public List<Mute> getAllActiveMutes(List<String> playerUuids) {
        List<Mute> mutes = new ArrayList<>();
        List<String> questionMarks = playerUuids.stream().map(p -> "?").collect(Collectors.toList());

        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(String.format("SELECT * FROM sp_muted_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers) + " AND player_uuid IN (%s)", String.join(", ", questionMarks)))) {
            ps.setLong(1, System.currentTimeMillis());
            int index = 2;
            for (String uuid : playerUuids) {
                ps.setString(index, uuid);
                index++;
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mutes.add(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return mutes;
    }

    @Override
    public Optional<Mute> findActiveMute(int muteId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE id = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers))) {
            ps.setInt(1, muteId);
            ps.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Mute> findActiveMute(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE player_uuid = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers))) {
            ps.setString(1, playerUuid.toString());
            ps.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Mute> getMute(int muteId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE id = ? " + getServerNameFilterWithAnd(muteSyncServers))) {
            ps.setInt(1, muteId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Mute> getAppealedMutes(int offset, int amount) {
        List<Mute> mutes = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT sp_muted_players.* FROM sp_muted_players INNER JOIN sp_appeals appeals on sp_muted_players.id = appeals.appealable_id AND appeals.status = 'OPEN' AND appeals.type = ? "
                 + getServerNameFilterWithWhere(options.serverSyncConfiguration.muteSyncServers) +
                 " ORDER BY sp_muted_players.creation_timestamp DESC LIMIT ?,?")
        ) {
            ps.setString(1, AppealableType.MUTE.name());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mutes.add(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return mutes;
    }

    @Override
    public List<Mute> getMutesForPlayer(UUID playerUuid) {
        List<Mute> mutes = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE player_uuid = ? " + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC")) {
            ps.setString(1, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mutes.add(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return mutes;
    }

    @Override
    public List<Mute> getMutesForPlayerPaged(UUID playerUuid, int offset, int amount) {
        List<Mute> mutes = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE player_uuid = ? " + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?")) {
            ps.setString(1, playerUuid.toString());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mutes.add(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return mutes;
    }


    @Override
    public List<UUID> getAllPermanentMutedPlayers() {
        List<UUID> result = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid FROM sp_muted_players WHERE end_timestamp IS NULL " + getServerNameFilterWithAnd(muteSyncServers) + " GROUP BY player_uuid")) {
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
    public Optional<Mute> getLastMute(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE player_uuid = ? " + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC")) {
            ps.setString(1, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public void setMuteDuration(int muteId, long newDuration) {
        try (Connection sql = getConnection();
             PreparedStatement update = sql.prepareStatement("UPDATE sp_muted_players set end_timestamp=? WHERE id=? " + getServerNameFilterWithAnd(muteSyncServers))) {
            update.setLong(1, newDuration);
            update.setInt(2, muteId);
            update.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        Map<UUID, Integer> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid, count(*) as count FROM sp_muted_players " + Constants.getServerNameFilterWithWhere(muteSyncServers) + " GROUP BY player_uuid ORDER BY count DESC")) {
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
    public Map<UUID, Long> getMuteDurationByPlayer() {
        Map<UUID, Long> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid, sum(end_timestamp - creation_timestamp) as count FROM sp_muted_players WHERE end_timestamp is not null " + getServerNameFilterWithAnd(muteSyncServers) + " GROUP BY player_uuid ORDER BY count DESC")) {
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
    public long getTotalCount() {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT count(*) as count FROM sp_muted_players " + Constants.getServerNameFilterWithWhere(muteSyncServers))) {
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
    public long getActiveCount() {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers))) {
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
    public void update(Mute mute) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_muted_players set unmuted_by_uuid=?, unmute_reason=?, end_timestamp=? WHERE ID=?")) {
            insert.setString(1, mute.getUnmutedByUuid().toString());
            insert.setString(2, mute.getUnmuteReason());
            insert.setLong(3, System.currentTimeMillis());
            insert.setInt(4, mute.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private Mute buildMute(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        UUID issuerUuid = UUID.fromString(rs.getString("issuer_uuid"));
        UUID unmutedByUUID = rs.getString("unmuted_by_uuid") != null ? UUID.fromString(rs.getString("unmuted_by_uuid")) : null;

        String playerName = rs.getString("player_name");
        String issuerName = rs.getString("issuer_name");

        String unmutedByName = null;
        if (unmutedByUUID != null) {
            unmutedByName = getPlayerName(unmutedByUUID);
        }

        int id = rs.getInt("ID");
        Long endTimestamp = rs.getLong("end_timestamp");
        endTimestamp = rs.wasNull() ? null : endTimestamp;
        String serverName = rs.getString("server_name") == null ? "[Unknown]" : rs.getString("server_name");

        List<Appeal> appeals = appealRepository.getAppeals(id, AppealableType.MUTE);

        return new Mute(
            id,
            rs.getString("reason"),
            rs.getLong("creation_timestamp"),
            endTimestamp,
            playerName,
            playerUuid,
            issuerName,
            issuerUuid,
            unmutedByName,
            unmutedByUUID,
            rs.getString("unmute_reason"),
            serverName,
            rs.getBoolean("soft_mute"),
            appeals.size() > 0 ? appeals.get(0) : null);
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

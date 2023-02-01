package net.shortninja.staffplus.core.punishments.mute.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.appeals.Appeal;
import net.shortninja.staffplus.core.punishments.mute.Mute;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.mute.MuteFilters;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithWhere;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.insertFilterValues;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.mapFilters;

@IocBean
public class MuteRepositoryImpl implements MuteRepository {

    private final PlayerManager playerManager;
    private final Options options;
    private final ServerSyncConfig muteSyncServers;
    private final QueryBuilderFactory query;

    public MuteRepositoryImpl(PlayerManager playerManager, Options options, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.options = options;
        this.muteSyncServers = options.serverSyncConfiguration.muteSyncServers;
        this.query = query;
    }

    @Override
    public int addMute(Mute mute) {
        return query.create().insertQuery("INSERT INTO sp_muted_players(reason, player_uuid, player_name, issuer_uuid, issuer_name, end_timestamp, creation_timestamp, server_name, soft_mute) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);", (insert) -> {
            insert.setString(1, mute.getReason());
            insert.setString(2, mute.getTargetUuid().toString());
            insert.setString(3, mute.getTargetName());
            insert.setString(4, mute.getIssuerUuid().toString());
            insert.setString(5, mute.getIssuerName());
            if (mute.getEndTimestamp() == null) {
                insert.setNull(6, Types.BIGINT);
            } else {
                insert.setLong(6, mute.getEndTimestamp());
            }
            insert.setLong(7, mute.getCreationTimestamp());
            insert.setString(8, options.serverName);
            insert.setBoolean(9, mute.isSoftMute());
        });
    }

    @Override
    public List<Mute> getActiveMutes(int offset, int amount) {
        return query.create().find("SELECT * FROM sp_muted_players m " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setLong(1, System.currentTimeMillis());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            }, this::buildMute);
    }

    @Override
    public List<Mute> getAllActiveMutes(List<String> playerUuids) {
        List<String> questionMarks = playerUuids.stream().map(p -> "?").collect(Collectors.toList());
        return query.create().find(String.format("SELECT * FROM sp_muted_players m " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers) + " AND player_uuid IN (%s)", String.join(", ", questionMarks)),
            (ps) -> {
                ps.setLong(1, System.currentTimeMillis());
                int index = 2;
                for (String uuid : playerUuids) {
                    ps.setString(index, uuid);
                    index++;
                }
            }, this::buildMute);
    }

    @Override
    public Optional<Mute> findActiveMute(int muteId) {
        return query.create().findOne("SELECT * FROM sp_muted_players m " +
            "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
            "WHERE m.id = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers), (ps) -> {
            ps.setInt(1, muteId);
            ps.setLong(2, System.currentTimeMillis());
        }, this::buildMute);
    }

    @Override
    public Optional<Mute> findActiveMute(UUID playerUuid) {
        return query.create().findOne("SELECT * FROM sp_muted_players m " +
            "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
            "WHERE player_uuid = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers), (ps) -> {
            ps.setString(1, playerUuid.toString());
            ps.setLong(2, System.currentTimeMillis());
        }, this::buildMute);
    }

    @Override
    public Optional<Mute> getMute(int muteId) {
        return query.create().findOne("SELECT * FROM sp_muted_players m " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE m.id = ? " + getServerNameFilterWithAnd(muteSyncServers),
            (ps) -> ps.setInt(1, muteId),
            this::buildMute);
    }

    @Override
    public List<Mute> getAppealedMutes(int offset, int amount) {
        return query.create().find(
            "SELECT sp_muted_players.* FROM sp_muted_players INNER JOIN sp_appeals appeals on sp_muted_players.id = appeals.appealable_id AND appeals.status = 'OPEN' AND appeals.type = ? " +
                "WHERE sp_muted_players.end_timestamp IS NULL OR sp_muted_players.end_timestamp > ? "
                + getServerNameFilterWithAnd(options.serverSyncConfiguration.muteSyncServers) +
                " ORDER BY sp_muted_players.creation_timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, AppealableType.MUTE.name());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
                ps.setLong(4, System.currentTimeMillis());
            }, this::buildMute);
    }

    @Override
    public List<Mute> getMutesForPlayer(UUID playerUuid) {
        return query.create().find(
            "SELECT * FROM sp_muted_players m " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE player_uuid = ? " + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC",
            (ps) -> ps.setString(1, playerUuid.toString()), this::buildMute);
    }

    @Override
    public List<Mute> getMyMutes(UUID playerUuid, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_muted_players m " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE player_uuid = ? AND soft_mute = ?" + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, playerUuid.toString());
                ps.setBoolean(2, false);
                ps.setInt(3, offset);
                ps.setInt(4, amount);
            },
            this::buildMute);
    }

    @Override
    public List<Mute> getMutesForPlayerPaged(UUID playerUuid, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_muted_players m " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE player_uuid = ? " + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, playerUuid.toString());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            },
            this::buildMute);
    }

    @Override
    public List<UUID> getAllPermanentMutedPlayers() {
        return query.create().find("SELECT player_uuid FROM sp_muted_players " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE end_timestamp IS NULL " + getServerNameFilterWithAnd(muteSyncServers) + " GROUP BY player_uuid",
            (rs) -> UUID.fromString(rs.getString("player_uuid")));
    }

    @Override
    public Optional<Mute> getLastMute(UUID playerUuid) {
        return query.create().findOne("SELECT * FROM sp_muted_players m " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE player_uuid = ? " + getServerNameFilterWithAnd(muteSyncServers) + " ORDER BY creation_timestamp DESC",
            (ps) -> ps.setString(1, playerUuid.toString()), this::buildMute);
    }

    @Override
    public void setMuteDuration(int muteId, long newDuration) {
        query.create().updateQuery("UPDATE sp_muted_players set end_timestamp=? WHERE id=? " + getServerNameFilterWithAnd(muteSyncServers),
            (update) -> {
                update.setLong(1, newDuration);
                update.setInt(2, muteId);
            });
    }

    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        return query.create().findMap("SELECT player_uuid, count(*) as count FROM sp_muted_players " + getServerNameFilterWithWhere(muteSyncServers) + " GROUP BY player_uuid ORDER BY count DESC",
            rs -> UUID.fromString(rs.getString("player_uuid")),
            rs -> rs.getInt("count"));
    }

    @Override
    public Map<UUID, Long> getMuteDurationByPlayer() {
        return query.create().findMap("SELECT player_uuid, sum(end_timestamp - creation_timestamp) as count FROM sp_muted_players WHERE end_timestamp is not null " + getServerNameFilterWithAnd(muteSyncServers) + " GROUP BY player_uuid ORDER BY count DESC",
            rs -> UUID.fromString(rs.getString("player_uuid")),
            rs -> rs.getLong("count"));
    }

    @Override
    public long getTotalCount() {
        return query.create().getOne("SELECT count(*) as count FROM sp_muted_players " + getServerNameFilterWithWhere(muteSyncServers), (rs) -> rs.getLong("count"));
    }

    @Override
    public long getMuteCount(MuteFilters muteFilters) {
        String filterQuery = mapFilters(muteFilters, false);
        String query = "SELECT count(*) as count FROM sp_muted_players WHERE " + filterQuery + getServerNameFilterWithAnd(options.serverSyncConfiguration.muteSyncServers);

        return this.query.create()
            .getOne(query, (ps) -> insertFilterValues(muteFilters, ps, 1), (rs) -> rs.getLong("count"));
    }

    @Override
    public long getActiveCount() {
        return query.create().getOne("SELECT * FROM sp_muted_players m " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = m.id AND type = 'MUTE' LIMIT 1) " +
                "WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(muteSyncServers),
            (ps) -> ps.setLong(1, System.currentTimeMillis()),
            (rs) -> rs.getLong("count"));
    }

    @Override
    public void update(Mute mute) {
        query.create().updateQuery("UPDATE sp_muted_players set unmuted_by_uuid=?, unmute_reason=?, end_timestamp=? WHERE ID=?", (insert) -> {
            insert.setString(1, mute.getUnmutedByUuid().toString());
            insert.setString(2, mute.getUnmuteReason());
            insert.setLong(3, System.currentTimeMillis());
            insert.setInt(4, mute.getId());
        });
    }

    private Mute buildMute(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        UUID playerUuid = UUID.fromString(rs.getString(2));
        UUID issuerUuid = UUID.fromString(rs.getString(3));
        UUID unmutedByUUID = rs.getString(4) != null ? UUID.fromString(rs.getString(4)) : null;
        String reason = rs.getString(5);
        String unmuteReason = rs.getString(6);
        long creationTimestamp = rs.getLong(7);
        Long endTimestamp = rs.getLong(8);
        endTimestamp = rs.wasNull() ? null : endTimestamp;
        String serverName = rs.getString(9) == null ? "[Unknown]" : rs.getString(9);

        String playerName = rs.getString(10);
        String issuerName = rs.getString(11);
        boolean softMute = rs.getBoolean(12);

        String unmutedByName = null;
        if (unmutedByUUID != null) {
            unmutedByName = getPlayerName(unmutedByUUID);
        }

        Appeal appeal = null;
        Integer appealId = rs.getInt(13);
        appealId = rs.wasNull() ? null : appealId;
        if(appealId != null) {
            int appealableId = rs.getInt(14);
            UUID appealerUuid = UUID.fromString(rs.getString(15));
            String resolverStringUuid = rs.getString(16);
            String appealReason = rs.getString(17);
            String resolveReason = rs.getString(18);
            AppealStatus status = AppealStatus.valueOf(rs.getString(19));
            long appealTimestamp = rs.getLong(20);
            AppealableType type = AppealableType.valueOf(rs.getString(21));
            String appealerName = rs.getString(22);

            UUID resolverUuid = null;
            String resolverName = null;
            if (StringUtils.isNotEmpty(resolverStringUuid)) {
                resolverUuid = UUID.fromString(resolverStringUuid);
                resolverName = rs.getString(23);
            }


            appeal = new Appeal(
                id,
                appealableId,
                appealerUuid,
                appealerName,
                resolverUuid,
                resolverName,
                appealReason,
                resolveReason, status,
                appealTimestamp,
                type);
        }

        return new Mute(
            id,
            reason,
            creationTimestamp,
            endTimestamp,
            playerName,
            playerUuid,
            issuerName,
            issuerUuid,
            unmutedByName,
            unmutedByUUID,
            unmuteReason,
            serverName,
            softMute,
            appeal);
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

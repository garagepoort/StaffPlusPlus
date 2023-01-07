package net.shortninja.staffplus.core.domain.staff.ban.playerbans.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.ban.BanFilters;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithWhere;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.insertFilterValues;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.mapFilters;

@IocBean
public class BansRepositoryImpl implements BansRepository {

    private final PlayerManager playerManager;
    private final Options options;
    private final QueryBuilderFactory query;

    public BansRepositoryImpl(PlayerManager playerManager, Options options, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.options = options;
        this.query = query;
    }

    @Override
    public int addBan(Ban ban) {
        return query.create().insertQuery("INSERT INTO sp_banned_players(reason, player_uuid, player_name, issuer_uuid,issuer_name, end_timestamp, creation_timestamp, server_name, silent_ban, template) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", (insert) -> {
            insert.setString(1, ban.getReason());
            insert.setString(2, ban.getTargetUuid().toString());
            insert.setString(3, ban.getTargetName());
            insert.setString(4, ban.getIssuerUuid().toString());
            insert.setString(5, ban.getIssuerName());
            if (ban.getEndTimestamp() == null) {
                insert.setNull(6, Types.BIGINT);
            } else {
                insert.setLong(6, ban.getEndTimestamp());
            }
            insert.setLong(7, ban.getCreationTimestamp());
            insert.setString(8, options.serverName);
            insert.setBoolean(9, ban.isSilentBan());
            Optional<String> optional = ban.getTemplate();
            if (optional.isPresent()) {
                insert.setObject(10, optional.get());
            } else {
                insert.setNull(10, Types.VARCHAR);
            }
        });
    }

    @Override
    public List<Ban> getActiveBans(int offset, int amount) {
        return query.create().find("SELECT * FROM sp_banned_players b " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = b.id AND type = 'BAN' LIMIT 1) " +
                "WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            ps -> {
                ps.setLong(1, System.currentTimeMillis());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            }, this::buildBan);
    }

    @Override
    public Optional<Ban> findActiveBan(int banId) {
        return query.create().findOne("SELECT * FROM sp_banned_players b " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = b.id AND type = 'BAN' LIMIT 1) " +
                " WHERE b.id = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            ps -> {
                ps.setInt(1, banId);
                ps.setLong(2, System.currentTimeMillis());
            }, this::buildBan);
    }

    @Override
    public Optional<Ban> findBan(String targetName, long creationTimestamp) {
        return query.create().findOne("SELECT * FROM sp_banned_players b " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = b.id AND type = 'BAN' LIMIT 1) " +
                "WHERE player_name = ? AND creation_timestamp = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            ps -> {
                ps.setString(1, targetName);
                ps.setLong(2, creationTimestamp);
            }, this::buildBan);
    }

    @Override
    public Optional<Ban> getBan(int banId) {
        return query.create().findOne("SELECT * FROM sp_banned_players b " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = b.id AND type = 'BAN' LIMIT 1) " +
                "WHERE b.id = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            ps -> ps.setInt(1, banId),
            this::buildBan);
    }

    @Override
    public Optional<Ban> findActiveBan(UUID playerUuid) {
        return query.create().findOne("SELECT * FROM sp_banned_players b " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = b.id AND type = 'BAN' LIMIT 1) " +
                "WHERE player_uuid = ? AND (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            ps -> {
                ps.setString(1, playerUuid.toString());
                ps.setLong(2, System.currentTimeMillis());
            }, this::buildBan);
    }

    @Override
    public List<Ban> getBansForPlayer(UUID playerUuid) {
        return query.create().find("SELECT * FROM sp_banned_players b " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = b.id AND type = 'BAN' LIMIT 1) " +
                "WHERE player_uuid = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " ORDER BY creation_timestamp DESC",
            ps -> ps.setString(1, playerUuid.toString()),
            this::buildBan);
    }

    @Override
    public List<Ban> getBansForPlayerPaged(UUID playerUuid, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_banned_players b " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = b.id AND type = 'BAN' LIMIT 1) " +
                "WHERE player_uuid = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            ps -> {
                ps.setString(1, playerUuid.toString());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            },
            this::buildBan);
    }

    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        return query.create().findMap("SELECT player_uuid, count(*) as count FROM sp_banned_players " + getServerNameFilterWithWhere(options.serverSyncConfiguration.banSyncServers) + " GROUP BY player_uuid ORDER BY count DESC",
            rs -> UUID.fromString(rs.getString("player_uuid")),
            rs -> rs.getInt("count"));
    }

    @Override
    public long getTotalCount() {
        return query.create().getOne("SELECT count(*) as count FROM sp_banned_players " + getServerNameFilterWithWhere(options.serverSyncConfiguration.banSyncServers),
            rs -> rs.getLong("count"));
    }

    @Override
    public void setBanDuration(int banId, long newDuration) {
        query.create().updateQuery("UPDATE sp_banned_players set end_timestamp=? WHERE id=? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            update -> {
                update.setLong(1, newDuration);
                update.setInt(2, banId);
            });
    }

    @Override
    public long getActiveCount() {
        return query.create().getOne("SELECT count(*) as count FROM sp_banned_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            ps -> ps.setLong(1, System.currentTimeMillis()),
            rs -> rs.getLong("count"));
    }

    @Override
    public List<Ban> getAppealedBans(int offset, int amount) {
        return query.create().find("SELECT sp_banned_players.* FROM sp_banned_players INNER JOIN sp_appeals appeals on sp_banned_players.id = appeals.appealable_id AND appeals.status = 'OPEN' AND appeals.type = ? "
                + getServerNameFilterWithWhere(options.serverSyncConfiguration.banSyncServers) +
                " ORDER BY sp_banned_players.creation_timestamp DESC LIMIT ?,?",
            ps -> {
                ps.setString(1, AppealableType.BAN.name());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            }, this::buildBan);
    }

    @Override
    public long getBanCount(BanFilters banFilters) {
        String filterQuery = mapFilters(banFilters, false);
        String query = "SELECT count(*) as count FROM sp_banned_players WHERE " + filterQuery + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers);

        return this.query.create()
            .getOne(query, (ps) -> insertFilterValues(banFilters, ps, 1), (rs) -> rs.getLong("count"));
    }

    @Override
    public Map<UUID, Long> getBanDurationByPlayer() {
        return query.create().findMap("SELECT player_uuid, sum(end_timestamp - creation_timestamp) as count FROM sp_banned_players WHERE end_timestamp is not null "
                + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers)
                + " GROUP BY player_uuid ORDER BY count DESC",
            rs -> UUID.fromString(rs.getString("player_uuid")),
            rs -> rs.getLong("count"));
    }

    @Override
    public List<UUID> getAllPermanentBannedPlayers() {
        return query.create().find("SELECT player_uuid FROM sp_banned_players WHERE end_timestamp IS NULL " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers) + " GROUP BY player_uuid",
            rs -> UUID.fromString(rs.getString("player_uuid")));
    }

    @Override
    public void update(Ban ban) {
        query.create().updateQuery("UPDATE sp_banned_players set unbanned_by_uuid=?, unban_reason=?, end_timestamp=?, silent_unban=? WHERE ID=?",
            insert -> {
                insert.setString(1, ban.getUnbannedByUuid().toString());
                insert.setString(2, ban.getUnbanReason());
                insert.setLong(3, System.currentTimeMillis());
                insert.setBoolean(4, ban.isSilentUnban());
                insert.setInt(5, ban.getId());
            });
    }

    private Ban buildBan(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        UUID playerUuid = UUID.fromString(rs.getString(2));
        UUID issuerUuid = UUID.fromString(rs.getString(3));
        UUID unbannedByUUID = rs.getString(4) != null ? UUID.fromString(rs.getString(4)) : null;
        String reason = rs.getString(5);
        String unbanReason = rs.getString(6);
        long creationTimestamp = rs.getLong(7);
        Long endTimestamp = rs.getLong(8);
        endTimestamp = rs.wasNull() ? null : endTimestamp;
        String serverName = rs.getString(9) == null ? "[Unknown]" : rs.getString(9);

        String playerName = rs.getString(10);
        String issuerName = rs.getString(11);
        boolean silentBan = rs.getBoolean(12);
        boolean silentUnban = rs.getBoolean(13);
        String template = rs.getString(14);

        String unbannedByName = null;
        if (unbannedByUUID != null) {
            unbannedByName = getPlayerName(unbannedByUUID);
        }

        Appeal appeal = null;
        Integer appealId = rs.getInt(15);
        appealId = rs.wasNull() ? null : appealId;
        if(appealId != null) {
            int appealableId = rs.getInt(16);
            UUID appealerUuid = UUID.fromString(rs.getString(17));
            String resolverStringUuid = rs.getString(18);
            String appealReason = rs.getString(19);
            String resolveReason = rs.getString(20);
            AppealStatus status = AppealStatus.valueOf(rs.getString(21));
            long appealTimestamp = rs.getLong(22);
            AppealableType type = AppealableType.valueOf(rs.getString(23));
            String appealerName = rs.getString(24);

            UUID resolverUuid = null;
            String resolverName = null;
            if (StringUtils.isNotEmpty(resolverStringUuid)) {
                resolverUuid = UUID.fromString(resolverStringUuid);
                resolverName = rs.getString(25);
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

        return new Ban(
            id,
            reason,
            creationTimestamp,
            endTimestamp,
            playerName,
            playerUuid,
            issuerName,
            issuerUuid,
            unbannedByName,
            unbannedByUUID,
            unbanReason,
            serverName, silentBan, silentUnban,
            template,
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

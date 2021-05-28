package net.shortninja.staffplus.core.domain.staff.ban.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.Ban;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;

public abstract class AbstractSqlBansRepository implements BansRepository {

    private final PlayerManager playerManager;
    private final SqlConnectionProvider sqlConnectionProvider;
    protected final Options options;
    private final FluentJdbc fluentJdbc;
    private final boolean banSyncEnabled;

    public AbstractSqlBansRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.playerManager = playerManager;
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.options = options;
        this.banSyncEnabled = options.serverSyncConfiguration.isBanSyncEnabled();
        this.fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(sqlConnectionProvider.getDatasource())
            .build();
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public List<Ban> getActiveBans(int offset, int amount) {
        return fluentJdbc.query()
            .select("SELECT * FROM sp_banned_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) "
                + getServerNameFilterWithAnd(banSyncEnabled)
                + " ORDER BY creation_timestamp DESC LIMIT ?,?")
            .params(System.currentTimeMillis(), offset, amount)
            .listResult(this::buildBan);
    }

    @Override
    public Optional<Ban> findActiveBan(int banId) {
        return fluentJdbc.query()
            .select("SELECT * FROM sp_banned_players WHERE id = ? AND (end_timestamp IS NULL OR end_timestamp > ?) "
                + getServerNameFilterWithAnd(banSyncEnabled))
            .params(banId, System.currentTimeMillis())
            .firstResult(this::buildBan);
    }

    @Override
    public Optional<Ban> findActiveBan(UUID playerUuid) {
        return fluentJdbc.query()
            .select("SELECT * FROM sp_banned_players WHERE player_uuid = ? AND (end_timestamp IS NULL OR end_timestamp > ?) "
                + getServerNameFilterWithAnd(banSyncEnabled))
            .params(playerUuid.toString(), System.currentTimeMillis())
            .firstResult(this::buildBan);
    }

    @Override
    public List<Ban> getBansForPlayer(UUID playerUuid) {
        return fluentJdbc.query()
            .select("SELECT * FROM sp_banned_players WHERE player_uuid = ? "
                + getServerNameFilterWithAnd(banSyncEnabled) + " ORDER BY creation_timestamp DESC")
            .params(playerUuid.toString())
            .listResult(this::buildBan);
    }

    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        Map<UUID, Integer> count = new HashMap<>();
        fluentJdbc.query()
            .select("SELECT player_uuid, count(*) as count FROM sp_banned_players "
                + Constants.getServerNameFilterWithWhere(banSyncEnabled)
                + " GROUP BY player_uuid ORDER BY count DESC")
            .iterateResult(rs -> count.put(UUID.fromString(rs.getString("player_uuid")), rs.getInt("count")));
        return count;
    }

    @Override
    public long getTotalCount() {
        return fluentJdbc.query()
            .select("SELECT count(*) as count FROM sp_banned_players " + Constants.getServerNameFilterWithWhere(banSyncEnabled))
            .firstResult(rs -> rs.getLong("count")).orElse(0L);
    }


    @Override
    public long getActiveCount() {
        return fluentJdbc.query()
            .select("SELECT count(*) as count FROM sp_banned_players WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(banSyncEnabled))
            .params(System.currentTimeMillis())
            .firstResult(rs -> rs.getLong("count")).orElse(0L);
    }

    @Override
    public Map<UUID, Long> getBanDurationByPlayer() {
        Map<UUID, Long> count = new HashMap<>();
        fluentJdbc.query()
            .select("SELECT player_uuid, sum(end_timestamp - creation_timestamp) as count FROM sp_banned_players WHERE end_timestamp is not null "
                + Constants.getServerNameFilterWithAnd(banSyncEnabled)
                + " GROUP BY player_uuid ORDER BY count DESC")
            .iterateResult(rs -> count.put(UUID.fromString(rs.getString("player_uuid")), rs.getLong("count")));
        return count;
    }

    @Override
    public List<UUID> getAllPermanentBannedPlayers() {
        return fluentJdbc.query()
            .select("SELECT player_uuid FROM sp_banned_players WHERE end_timestamp IS NULL " + Constants.getServerNameFilterWithAnd(banSyncEnabled) + " GROUP BY player_uuid")
            .listResult(rs -> UUID.fromString(rs.getString("player_uuid")));
    }

    @Override
    public void update(Ban ban) {
        fluentJdbc.query().update("UPDATE sp_banned_players set unbanned_by_uuid=?, unban_reason=?, end_timestamp=? WHERE ID=?")
            .params(ban.getUnbannedByUuid().toString(), ban.getUnbanReason(), System.currentTimeMillis(), ban.getId())
            .run();
    }

    private Ban buildBan(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        UUID issuerUuid = UUID.fromString(rs.getString("issuer_uuid"));
        UUID unbannedByUUID = rs.getString("unbanned_by_uuid") != null ? UUID.fromString(rs.getString("unbanned_by_uuid")) : null;

        String playerName = rs.getString("player_name");
        String issuerName = rs.getString("issuer_name");

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

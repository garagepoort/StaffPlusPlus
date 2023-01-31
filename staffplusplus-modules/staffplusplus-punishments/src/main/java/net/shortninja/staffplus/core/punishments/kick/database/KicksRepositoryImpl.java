package net.shortninja.staffplus.core.punishments.kick.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.punishments.kick.Kick;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;

@IocBean
public class KicksRepositoryImpl implements KicksRepository {

    private static final String PLAYER_UUID_COLUMN = "player_uuid";
    private static final String ISSUER_UUID_COLUMN = "issuer_uuid";
    private static final String SERVER_NAME_COLUMN = "server_name";
    private static final String REASON_COLUMN = "reason";
    private static final String CREATION_TIMESTAMP_COLUMN = "creation_timestamp";
    private static final String ID_COLUMN = "ID";

    private final PlayerManager playerManager;
    private final Options options;
    private final String serverNameFilter;
    private final QueryBuilderFactory query;

    public KicksRepositoryImpl(PlayerManager playerManager, Options options, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.options = options;
        this.serverNameFilter = !options.serverSyncConfiguration.kickSyncServers.isEnabled() ? "AND (server_name is null OR server_name='" + options.serverName + "')" : "";
        this.query = query;
    }

    @Override
    public int addKick(Kick kick) {
        return query.create().insertQuery("INSERT INTO sp_kicked_players(reason, player_uuid, issuer_uuid, creation_timestamp, server_name) " +
            "VALUES(?, ?, ?, ?, ?);", (insert) -> {
            insert.setString(1, kick.getReason());
            insert.setString(2, kick.getTargetUuid().toString());
            insert.setString(3, kick.getIssuerUuid().toString());
            insert.setLong(4, kick.getCreationTimestamp());
            insert.setString(5, options.serverName);
        });
    }

    @Override
    public List<Kick> getKicksForPlayer(UUID playerUuid) {
        return query.create().find("SELECT * FROM sp_kicked_players WHERE player_uuid = ? " + serverNameFilter + " ORDER BY creation_timestamp DESC",
            (ps) -> ps.setString(1, playerUuid.toString()),
            this::buildKick);
    }


    @Override
    public List<Kick> getKicksForPlayer(UUID playerUuid, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_kicked_players WHERE player_uuid = ? " + serverNameFilter + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            ps -> {
                ps.setString(1, playerUuid.toString());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            },
            this::buildKick);
    }


    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        return query.create().findMap("SELECT player_uuid, count(*) as count FROM sp_kicked_players " + Constants.getServerNameFilterWithWhere(options.serverSyncConfiguration.kickSyncServers) + " GROUP BY player_uuid ORDER BY count DESC",
            rs -> UUID.fromString(rs.getString(PLAYER_UUID_COLUMN)),
            rs -> rs.getInt("count"));
    }

    private Kick buildKick(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString(PLAYER_UUID_COLUMN));
        UUID issuerUuid = UUID.fromString(rs.getString(ISSUER_UUID_COLUMN));

        String playerName = getPlayerName(playerUuid);
        String issuerName = getPlayerName(issuerUuid);

        int id = rs.getInt(ID_COLUMN);
        String serverName = rs.getString(SERVER_NAME_COLUMN) == null ? "[Unknown]" : rs.getString(SERVER_NAME_COLUMN);

        return new Kick(
            id,
            rs.getString(REASON_COLUMN),
            rs.getLong(CREATION_TIMESTAMP_COLUMN),
            playerName,
            playerUuid,
            issuerName,
            issuerUuid,
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

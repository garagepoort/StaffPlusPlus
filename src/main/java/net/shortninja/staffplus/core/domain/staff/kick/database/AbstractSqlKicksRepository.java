package net.shortninja.staffplus.core.domain.staff.kick.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.kick.Kick;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;

public abstract class AbstractSqlKicksRepository implements KicksRepository {

    private static final String PLAYER_UUID_COLUMN = "player_uuid";
    private static final String ISSUER_UUID_COLUMN = "issuer_uuid";
    private static final String SERVER_NAME_COLUMN = "server_name";
    private static final String REASON_COLUMN = "reason";
    private static final String CREATION_TIMESTAMP_COLUMN = "creation_timestamp";
    public static final String ID_COLUMN = "ID";

    private final PlayerManager playerManager;
    private final SqlConnectionProvider sqlConnectionProvider;
    protected final Options options;
    private final String serverNameFilter;

    public AbstractSqlKicksRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.playerManager = playerManager;
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.options = options;
        serverNameFilter = !options.serverSyncConfiguration.isKickSyncEnabled() ? "AND (server_name is null OR server_name='" + options.serverName + "')" : "";
    }

    protected Connection getConnection(){
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public List<Kick> getKicksForPlayer(UUID playerUuid) {
        List<Kick> kicks = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_kicked_players WHERE player_uuid = ? " + serverNameFilter + " ORDER BY creation_timestamp DESC")) {
            ps.setString(1, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    kicks.add(buildKick(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return kicks;
    }

    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        Map<UUID, Integer> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT player_uuid, count(*) as count FROM sp_kicked_players " + Constants.getServerNameFilterWithWhere(options.serverSyncConfiguration.isKickSyncEnabled()) + " GROUP BY player_uuid ORDER BY count DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count.put(UUID.fromString(rs.getString(PLAYER_UUID_COLUMN)), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return count;
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

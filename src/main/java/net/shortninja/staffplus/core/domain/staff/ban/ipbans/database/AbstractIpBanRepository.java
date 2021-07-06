package net.shortninja.staffplus.core.domain.staff.ban.ipbans.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;

public abstract class AbstractIpBanRepository implements IpBanRepository {

    protected final SqlConnectionProvider sqlConnectionProvider;
    protected final Options options;
    private final PlayerManager playerManager;

    public AbstractIpBanRepository(SqlConnectionProvider sqlConnectionProvider, Options options, PlayerManager playerManager) {
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.options = options;
        this.playerManager = playerManager;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public List<IpBan> getBannedIps() {
        List<IpBan> bans = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_ips WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.isBanSyncEnabled()))) {
            ps.setLong(1, System.currentTimeMillis());

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
    public Optional<IpBan> getBannedRule(String ipAddress) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_banned_ips WHERE ip = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.isBanSyncEnabled()))) {
            ps.setString(1, ipAddress);
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
    public void deleteBan(IpBan ipBan) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_banned_ips WHERE id = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.isBanSyncEnabled()));) {
            insert.setLong(1, ipBan.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private IpBan buildBan(ResultSet rs) throws SQLException {
        UUID issuerUuid = UUID.fromString(rs.getString("issuer_uuid"));
        UUID unbannedByUUID = rs.getString("unbanned_by_uuid") != null ? UUID.fromString(rs.getString("unbanned_by_uuid")) : null;

        String issuerName = rs.getString("issuer_name");
        boolean silentBan = rs.getBoolean("silent_ban");
        boolean silentUnban = rs.getBoolean("silent_unban");

        String unbannedByName = null;
        if (unbannedByUUID != null) {
            unbannedByName = getPlayerName(unbannedByUUID);
        }

        long id = rs.getLong("id");
        Long endTimestamp = rs.getLong("end_timestamp");
        endTimestamp = rs.wasNull() ? null : endTimestamp;
        String serverName = rs.getString("server_name") == null ? "[Unknown]" : rs.getString("server_name");

        return new IpBan(
            id,
            rs.getString("ip"),
            issuerName,
            issuerUuid,
            unbannedByName,
            unbannedByUUID,
            silentBan,
            silentUnban,
            rs.getLong("creation_timestamp"),
            endTimestamp,
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

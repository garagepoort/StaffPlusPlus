package net.shortninja.staffplus.core.punishments.ban.ipbans.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.punishments.ban.ipbans.IpBan;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;

@IocBean
public class IpBanRepositoryImpl implements IpBanRepository {

    private final Options options;
    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public IpBanRepositoryImpl(Options options, PlayerManager playerManager, QueryBuilderFactory query) {
        this.options = options;
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public Long saveBan(IpBan ipBan) {
        return (long) query.create().insertQuery("INSERT INTO sp_banned_ips(ip, issuer_uuid, issuer_name, end_timestamp, creation_timestamp, server_name, silent_ban, template) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?);", (insert) -> {
            insert.setString(1, ipBan.getIp());
            insert.setString(2, ipBan.getIssuerUuid().toString());
            insert.setString(3, ipBan.getIssuerName());
            if (!ipBan.getEndTimestamp().isPresent()) {
                insert.setNull(4, Types.BIGINT);
            } else {
                insert.setLong(4, ipBan.getEndTimestamp().get());
            }
            insert.setLong(5, ipBan.getCreationDate());
            insert.setString(6, options.serverName);
            insert.setBoolean(7, ipBan.isSilentBan());
            Optional<String> optional = ipBan.getTemplate();
            if (optional.isPresent()) {
                insert.setObject(8, optional.get());
            } else {
                insert.setNull(8, Types.VARCHAR);
            }
        });
    }

    @Override
    public List<IpBan> getBannedIps() {
        return query.create().find("SELECT * FROM sp_banned_ips WHERE (end_timestamp IS NULL OR end_timestamp > ?) " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            ps -> ps.setLong(1, System.currentTimeMillis()),
            this::buildBan);
    }

    @Override
    public Optional<IpBan> getActiveBannedRule(String ipAddress) {
        return query.create().findOne("SELECT * FROM sp_banned_ips WHERE ip = ? AND (end_timestamp IS NULL OR end_timestamp > ?)" + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            ps -> {
                ps.setString(1, ipAddress);
                ps.setLong(2, System.currentTimeMillis());
            }, this::buildBan);
    }

    @Override
    public void deleteBan(IpBan ipBan) {
        query.create().deleteQuery("DELETE FROM sp_banned_ips WHERE id = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.banSyncServers),
            insert -> insert.setLong(1, ipBan.getId()));
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
            serverName,
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

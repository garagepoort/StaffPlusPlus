package net.shortninja.staffplus.core.domain.staff.ban.ipbans.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlIpBansRepository extends AbstractIpBanRepository {

    public MysqlIpBansRepository(SqlConnectionProvider sqlConnectionProvider, Options options, PlayerManager playerManager) {
        super(sqlConnectionProvider, options, playerManager);
    }

    @Override
    public Long saveBan(IpBan ipBan) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_banned_ips(ip, issuer_uuid, issuer_name, end_timestamp, creation_timestamp, server_name, silent_ban, template) " +
                 "VALUES(?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
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
            insertIfPresent(insert, 8, ipBan.getTemplate(), Types.VARCHAR);
            insert.executeUpdate();

            return Long.valueOf(getGeneratedId(sql, insert));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

package net.shortninja.staffplus.core.domain.staff.ban.ipbans.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteIpBansRepository extends AbstractIpBanRepository {


    public SqliteIpBansRepository(SqlConnectionProvider sqlConnectionProvider, Options options, PlayerManager playerManager) {
        super(sqlConnectionProvider, options, playerManager);
    }

    @Override
    public Long saveBan(IpBan ipBan) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_banned_ips(ip, issuer_uuid, issuer_name, end_timestamp, creation_timestamp, server_name, silent_ban) " +
                 "VALUES(?, ?, ?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
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
            insert.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
            long generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }
            connection.commit(); // Commits transaction.

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

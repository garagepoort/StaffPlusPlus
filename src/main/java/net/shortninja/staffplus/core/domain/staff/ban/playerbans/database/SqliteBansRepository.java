package net.shortninja.staffplus.core.domain.staff.ban.playerbans.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteBansRepository extends AbstractSqlBansRepository {

    public SqliteBansRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        super(playerManager, sqlConnectionProvider, options);
    }

    @Override
    public int addBan(Ban ban) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_banned_players(reason, player_uuid, player_name, issuer_uuid, issuer_name, end_timestamp, creation_timestamp, server_name, silent_ban) " +
                 "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
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
            insert.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
            int generatedKey = -1;
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

package net.shortninja.staffplus.domain.staff.ban.database;

import net.shortninja.staffplus.application.database.migrations.sqlite.SqlLiteConnection;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.exceptions.DatabaseException;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.staff.ban.Ban;

import java.sql.*;

public class SqliteBansRepository extends AbstractSqlBansRepository {

    public SqliteBansRepository(PlayerManager playerManager, Options options) {
        super(playerManager, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

    @Override
    public int addBan(Ban ban) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_banned_players(reason, player_uuid, issuer_uuid, end_timestamp, creation_timestamp, server_name) " +
                 "VALUES(?, ?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setString(1, ban.getReason());
            insert.setString(2, ban.getTargetUuid().toString());
            insert.setString(3, ban.getIssuerUuid().toString());
            if (ban.getEndTimestamp() == null) {
                insert.setNull(4, Types.BIGINT);
            } else {
                insert.setLong(4, ban.getEndTimestamp());
            }
            insert.setLong(5, ban.getCreationTimestamp());
            insert.setString(6, options.serverName);
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

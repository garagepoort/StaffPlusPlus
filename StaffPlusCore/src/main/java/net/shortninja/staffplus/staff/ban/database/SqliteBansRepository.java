package net.shortninja.staffplus.staff.ban.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.staff.ban.Ban;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.*;

public class SqliteBansRepository extends AbstractSqlBansRepository {

    public SqliteBansRepository(PlayerManager playerManager) {
        super(playerManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

    @Override
    public int addBan(Ban ban) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_banned_players(reason, player_uuid, issuer_uuid, end_timestamp, creation_timestamp) " +
                 "VALUES(?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setString(1, ban.getReason());
            insert.setString(2, ban.getPlayerUuid().toString());
            insert.setString(3, ban.getIssuerUuid().toString());
            if (ban.getEndDate() == null) {
                insert.setNull(4, Types.BIGINT);
            } else {
                insert.setLong(4, ban.getEndDate());
            }
            insert.setLong(5, ban.getCreationTimestamp());
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
            throw new RuntimeException(e);
        }
    }

}

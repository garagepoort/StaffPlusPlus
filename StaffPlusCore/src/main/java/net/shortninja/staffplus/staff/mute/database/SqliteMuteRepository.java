package net.shortninja.staffplus.staff.mute.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.mute.Mute;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.*;

public class SqliteMuteRepository extends AbstractSqlMuteRepository {

    public SqliteMuteRepository(PlayerManager playerManager, Options options) {
        super(playerManager, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

    @Override
    public int addMute(Mute mute) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_muted_players(reason, player_uuid, issuer_uuid, end_timestamp, creation_timestamp, server_name) " +
                 "VALUES(?, ?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setString(1, mute.getReason());
            insert.setString(2, mute.getTargetUuid().toString());
            insert.setString(3, mute.getIssuerUuid().toString());
            if (mute.getEndTimestamp() == null) {
                insert.setNull(4, Types.BIGINT);
            } else {
                insert.setLong(4, mute.getEndTimestamp());
            }
            insert.setLong(5, mute.getCreationTimestamp());
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
            throw new RuntimeException(e);
        }
    }

}

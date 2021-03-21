package net.shortninja.staffplus.domain.staff.kick.database;

import net.shortninja.staffplus.application.database.migrations.sqlite.SqlLiteConnection;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.exceptions.DatabaseException;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.staff.kick.Kick;

import java.sql.*;

public class SqliteKicksRepository extends AbstractSqlKicksRepository {

    public SqliteKicksRepository(PlayerManager playerManager, Options options) {
        super(playerManager, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

    @Override
    public int addKick(Kick kick) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_kicked_players(reason, player_uuid, issuer_uuid, creation_timestamp, server_name) " +
                 "VALUES(?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setString(1, kick.getReason());
            insert.setString(2, kick.getTargetUuid().toString());
            insert.setString(3, kick.getIssuerUuid().toString());
            insert.setLong(4, kick.getCreationTimestamp());
            insert.setString(5, options.serverName);
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

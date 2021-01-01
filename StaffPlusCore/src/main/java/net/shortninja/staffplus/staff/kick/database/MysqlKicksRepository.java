package net.shortninja.staffplus.staff.kick.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.staff.kick.Kick;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.*;

public class MysqlKicksRepository extends AbstractSqlKicksRepository {

    public MysqlKicksRepository(PlayerManager playerManager) {
        super(playerManager);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }

    @Override
    public int addKick(Kick kick) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_kicked_players(reason, player_uuid, issuer_uuid, creation_timestamp) " +
                 "VALUES(?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, kick.getReason());
            insert.setString(2, kick.getPlayerUuid().toString());
            insert.setString(3, kick.getIssuerUuid().toString());
            insert.setLong(4, kick.getCreationTimestamp());
            insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

            return generatedKey;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

package net.shortninja.staffplus.domain.staff.kick.database;

import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.kick.Kick;
import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;

import java.sql.*;

public class MysqlKicksRepository extends AbstractSqlKicksRepository {

    public MysqlKicksRepository(PlayerManager playerManager, Options options) {
        super(playerManager, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }

    @Override
    public int addKick(Kick kick) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_kicked_players(reason, player_uuid, issuer_uuid, creation_timestamp, server_name) " +
                 "VALUES(?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, kick.getReason());
            insert.setString(2, kick.getTargetUuid().toString());
            insert.setString(3, kick.getIssuerUuid().toString());
            insert.setLong(4, kick.getCreationTimestamp());
            insert.setString(5, options.serverName);
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

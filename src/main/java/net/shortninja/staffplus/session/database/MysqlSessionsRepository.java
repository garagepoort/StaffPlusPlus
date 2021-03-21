package net.shortninja.staffplus.session.database;

import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;
import net.shortninja.staffplus.common.exceptions.DatabaseException;

import java.sql.*;

public class MysqlSessionsRepository extends AbstractSqlSessionsRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }

    @Override
    public int addSession(SessionEntity sessionEntity) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_sessions(player_uuid, vanish_type, in_staff_mode) " +
                 "VALUES(?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, sessionEntity.getPlayerUuid().toString());
            insert.setString(2, sessionEntity.getVanishType().toString());
            insert.setBoolean(3, sessionEntity.getStaffMode());
            insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}

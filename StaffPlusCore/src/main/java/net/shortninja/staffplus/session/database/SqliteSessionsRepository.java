package net.shortninja.staffplus.session.database;

import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.*;

public class SqliteSessionsRepository extends AbstractSqlSessionsRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
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

            Statement statement = sql.createStatement();
            ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }
            sql.commit(); // Commits transaction.
            return generatedKey;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

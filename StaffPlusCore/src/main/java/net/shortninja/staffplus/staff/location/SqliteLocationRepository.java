package net.shortninja.staffplus.staff.location;

import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;
import org.bukkit.Location;

import java.sql.*;

public class SqliteLocationRepository extends AbstractSqlLocationRepository {

    public SqliteLocationRepository(Options options) {
        super(options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

    @Override
    public int addLocation(Location location) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_locations(x, y, z, world, server_name) " +
                 "VALUES(?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setInt(1, location.getBlockX());
            insert.setInt(2, location.getBlockY());
            insert.setInt(3, location.getBlockZ());
            insert.setString(4, location.getWorld().getName());
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
            throw new RuntimeException(e);
        }
    }
}

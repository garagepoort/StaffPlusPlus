package net.shortninja.staffplus.staff.location;

import net.shortninja.staffplus.IocContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.*;
import java.util.Optional;

public abstract class AbstractSqlLocationRepository implements LocationRepository, IocContainer.Repository {

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public int addLocation(Location location) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_locations(x, y, z, world) " +
                 "VALUES(?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setInt(1, location.getBlockX());
            insert.setInt(2, location.getBlockY());
            insert.setInt(3, location.getBlockZ());
            insert.setString(4, location.getWorld().getName());
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

    @Override
    public void removeLocation(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_locations WHERE ID = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Location> findLocation(int locationId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_locations WHERE ID = ?")) {
            ps.setInt(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildLocation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Location buildLocation(ResultSet rs) throws SQLException {
        World world = Bukkit.getWorld(rs.getString("name"));
        return new Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
    }

}

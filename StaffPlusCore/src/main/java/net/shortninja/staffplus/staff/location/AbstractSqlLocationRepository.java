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

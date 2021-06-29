package net.shortninja.staffplus.core.domain.location;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public abstract class AbstractSqlLocationRepository implements LocationRepository {

    protected final Options options;
    public final SqlConnectionProvider sqlConnectionProvider;

    public AbstractSqlLocationRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        this.options = options;
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    protected Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public void removeLocation(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_locations WHERE ID = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
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
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    private Location buildLocation(ResultSet rs) throws SQLException {
        World world = Bukkit.getWorld(rs.getString("name"));
        return new Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
    }

}

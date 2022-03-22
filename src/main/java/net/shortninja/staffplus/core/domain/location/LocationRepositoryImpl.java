package net.shortninja.staffplus.core.domain.location;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@IocBean
public class LocationRepositoryImpl implements LocationRepository {

    private final Options options;
    private final QueryBuilderFactory query;

    public LocationRepositoryImpl(Options options, QueryBuilderFactory query) {
        this.options = options;
        this.query = query;
    }

    @Override
    public int addLocation(Location location) {
        return query.create().insertQuery("INSERT INTO sp_locations(x, y, z, world, server_name) " +
            "VALUES(?, ?, ?, ?, ?);", (insert) -> {
            insert.setInt(1, location.getBlockX());
            insert.setInt(2, location.getBlockY());
            insert.setInt(3, location.getBlockZ());
            insert.setString(4, location.getWorld().getName());
            insert.setString(5, options.serverName);
        });
    }

    @Override
    public void removeLocation(int id) {
        query.create().deleteQuery("DELETE FROM sp_locations WHERE ID = ?", (insert) -> insert.setInt(1, id));
    }

    @Override
    public Optional<Location> findLocation(int locationId) {
        return query.create().findOne("SELECT * FROM sp_locations WHERE ID = ?",
            (ps) -> ps.setInt(1, locationId),
            this::buildLocation);
    }

    private Location buildLocation(ResultSet rs) throws SQLException {
        World world = Bukkit.getWorld(rs.getString("name"));
        return new Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
    }
}

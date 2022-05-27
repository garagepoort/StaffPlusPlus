package net.shortninja.staffplus.core.domain.location;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import org.bukkit.Location;

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
    public int addLocation(SppLocation location) {
        return query.create().insertQuery("INSERT INTO sp_locations(x, y, z, world, server_name) " +
            "VALUES(?, ?, ?, ?, ?);", (insert) -> {
            insert.setDouble(1, location.getX());
            insert.setDouble(2, location.getY());
            insert.setDouble(3, location.getZ());
            insert.setString(4, location.getWorldName());
            insert.setString(5, location.getServerName());
        });
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
    public void updateLocation(SppLocation location) {
        query.create().insertQuery("UPDATE sp_locations SET x = ?, y = ?, z = ?, world = ?, server_name = ? WHERE ID = ?",
            (update) -> {
                update.setDouble(1, location.getX());
                update.setDouble(2, location.getY());
                update.setDouble(3, location.getZ());
                update.setString(4, location.getWorldName());
                update.setString(5, location.getServerName());
                update.setInt(6, location.getId());
            });
    }

    @Override
    public void removeLocation(int id) {
        query.create().deleteQuery("DELETE FROM sp_locations WHERE ID = ?", (insert) -> insert.setInt(1, id));
    }

    @Override
    public Optional<SppLocation> findLocation(int locationId) {
        return query.create().findOne("SELECT * FROM sp_locations WHERE ID = ?",
            (ps) -> ps.setInt(1, locationId),
            this::buildLocation);
    }

    private SppLocation buildLocation(ResultSet rs) throws SQLException {
        return new SppLocation(rs.getInt("ID"),
            rs.getString("name"),
            rs.getDouble("x"),
            rs.getDouble("y"),
            rs.getDouble("z"),
            rs.getString("server_name"));
    }
}

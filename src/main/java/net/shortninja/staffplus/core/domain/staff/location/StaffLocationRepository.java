package net.shortninja.staffplus.core.domain.staff.location;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.location.LocationRepository;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithWhere;

@IocBean
public class StaffLocationRepository {

    private final LocationRepository locationRepository;
    private final QueryBuilderFactory query;
    private final Options options;

    public StaffLocationRepository(LocationRepository locationRepository, QueryBuilderFactory query, Options options) {
        this.locationRepository = locationRepository;
        this.query = query;
        this.options = options;
    }

    public int saveStaffLocation(Player player, StaffLocation staffLocation) {
        int locationId = locationRepository.addLocation(staffLocation.getLocation());

        return query.create()
            .insertQuery("INSERT INTO sp_staff_locations(name, location_id, creator_name, creator_uuid, server_name, creation_timestamp) VALUES (?, ?, ?, ?, ?, ?)", insert -> {
                insert.setString(1, staffLocation.getName());
                insert.setLong(2, locationId);
                insert.setString(3, player.getName());
                insert.setString(4, player.getUniqueId().toString());
                insert.setString(5, options.serverName);
                insert.setLong(6, staffLocation.getCreationTimestamp());
            });
    }

    public List<StaffLocation> getStaffLocations(int offset, int amount) {
        return query.create().find("SELECT * FROM sp_staff_locations INNER JOIN sp_locations l on sp_staff_locations.location_id = l.id" + getServerNameFilterWithWhere("sp_staff_locations", options.serverSyncConfiguration.staffLocationSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            ps -> {
                ps.setInt(1, offset);
                ps.setInt(2, amount);
            }, this::buildStaffLocation);
    }

    private StaffLocation buildStaffLocation(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        String name = resultSet.getString("name");
        long creationTimestamp = resultSet.getLong("creation_timestamp");

        UUID creatorUuid = UUID.fromString(resultSet.getString("creator_uuid"));
        String creatorName = resultSet.getString("creator_name");
        String serverName = resultSet.getString("server_name");

        double locationX = resultSet.getDouble(9);
        double locationY = resultSet.getDouble(10);
        double locationZ = resultSet.getDouble(11);
        String worldName = resultSet.getString(12);
        World locationWorld = Bukkit.getServer().getWorld(worldName);
        Location location = new Location(locationWorld, locationX, locationY, locationZ);
        return new StaffLocation(id, name, creatorName, creatorUuid, location, serverName, creationTimestamp);
    }

    public Optional<StaffLocation> getById(int locationId) {
        return query.create().findOne("SELECT * FROM sp_staff_locations INNER JOIN sp_locations l on sp_staff_locations.location_id = l.id WHERE sp_staff_locations.ID = ? " + getServerNameFilterWithAnd("sp_staff_locations", options.serverSyncConfiguration.staffLocationSyncServers),
            ps -> ps.setLong(1, locationId), this::buildStaffLocation);
    }

    public void delete(int locationId) {
        query.create().deleteQuery("DELETE FROM sp_staff_locations WHERE ID = ?", delete -> delete.setInt(1, locationId));
    }
}

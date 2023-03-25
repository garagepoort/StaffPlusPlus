package net.shortninja.staffplus.core.stafflocations;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.location.LocationRepository;
import net.shortninja.staffplus.core.domain.location.SppLocation;
import net.shortninja.staffplusplus.stafflocations.StaffLocationFilters;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithWhere;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.insertFilterValues;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.mapFilters;

@IocBean
public class StaffLocationRepository {

    public static final String BASE_QUERY = "SELECT sp_staff_locations.*, l.*, c.* FROM sp_staff_locations INNER JOIN sp_locations l on sp_staff_locations.location_id = l.id " +
        " LEFT JOIN (SELECT staff_location_id, max(timestamp) as note_timestamp FROM sp_staff_location_notes group by staff_location_id) newestNote on newestNote.staff_location_id = sp_staff_locations.id " +
        " LEFT JOIN sp_staff_location_notes c on c.staff_location_id = sp_staff_locations.id AND c.timestamp = newestNote.note_timestamp ";
    private final LocationRepository locationRepository;
    private final QueryBuilderFactory query;
    private final Options options;

    public StaffLocationRepository(LocationRepository locationRepository, QueryBuilderFactory query, Options options) {
        this.locationRepository = locationRepository;
        this.query = query;
        this.options = options;
    }

    public int saveStaffLocation(Player player, StaffLocation staffLocation) {
        int locationId = locationRepository.addLocation(staffLocation.getSppLocation());

        return query.create()
            .insertQuery("INSERT INTO sp_staff_locations(name, location_id, icon, creator_name, creator_uuid, server_name, creation_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)", insert -> {
                insert.setString(1, staffLocation.getName());
                insert.setLong(2, locationId);
                insert.setString(3, staffLocation.getIcon().name());
                insert.setString(4, player.getName());
                insert.setString(5, player.getUniqueId().toString());
                insert.setString(6, options.serverName);
                insert.setLong(7, staffLocation.getCreationTimestamp());
            });
    }

    public void updateStaffLocation(StaffLocation staffLocation) {
        locationRepository.updateLocation(staffLocation.getSppLocation());
        query.create()
            .updateQuery("UPDATE sp_staff_locations SET name = ? , icon = ? WHERE ID = ?",
                update -> {
                    update.setString(1, staffLocation.getName());
                    update.setString(2, staffLocation.getIcon().name());
                    update.setLong(3, staffLocation.getId());
                });
    }

    public List<StaffLocation> getStaffLocations(int offset, int amount) {
        return query.create().find(BASE_QUERY + getServerNameFilterWithWhere("sp_staff_locations", options.serverSyncConfiguration.staffLocationSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            ps -> {
                ps.setInt(1, offset);
                ps.setInt(2, amount);
            }, this::buildStaffLocation);
    }

    public List<StaffLocation> findLocations(StaffLocationFilters staffLocationFilters, int offset, int amount) {
        String filterQuery = mapFilters(staffLocationFilters, false);
        String query = BASE_QUERY + " WHERE " + filterQuery + getServerNameFilterWithAnd("sp_staff_locations", options.serverSyncConfiguration.staffLocationSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?";
        return this.query.create().find(query, (ps) -> {
            int index = 1;
            index = insertFilterValues(staffLocationFilters, ps, index);
            ps.setInt(index, offset);
            ps.setInt(index + 1, amount);
        }, this::buildStaffLocation);
    }

    private StaffLocation buildStaffLocation(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        String name = resultSet.getString("name");
        long creationTimestamp = resultSet.getLong("creation_timestamp");

        UUID creatorUuid = UUID.fromString(resultSet.getString("creator_uuid"));
        String creatorName = resultSet.getString("creator_name");
        String serverName = resultSet.getString("server_name");
        Material icon = JavaUtils.isValidEnum(Material.class, resultSet.getString("icon")) ? Material.valueOf(resultSet.getString("icon")) : Material.PAPER;

        int locationId = resultSet.getInt(9);
        double locationX = resultSet.getDouble(10);
        double locationY = resultSet.getDouble(11);
        double locationZ = resultSet.getDouble(12);
        String worldName = resultSet.getString(13);
        String locationServerName = resultSet.getString(14);

        SppLocation location = new SppLocation(locationId, worldName, locationX, locationY, locationZ, locationServerName);

        StaffLocationNote staffLocationNote = null;
        resultSet.getInt(15);
        if (!resultSet.wasNull()) {
            int noteId = resultSet.getInt(15);
            int staffLocationId = resultSet.getInt(16);
            String note = resultSet.getString(17);
            UUID linkedByUuid = UUID.fromString(resultSet.getString(18));
            String notedByName = resultSet.getString(19);
            long timestamp = resultSet.getLong(20);
            staffLocationNote = new StaffLocationNote(
                noteId,
                staffLocationId,
                note,
                linkedByUuid,
                notedByName,
                timestamp);
        }

        return new StaffLocation(id, name, creatorName, creatorUuid, location, serverName, creationTimestamp, staffLocationNote, icon);
    }

    public Optional<StaffLocation> getById(int locationId) {
        return query.create().findOne(BASE_QUERY + " WHERE sp_staff_locations.ID = ? " + getServerNameFilterWithAnd("sp_staff_locations", options.serverSyncConfiguration.staffLocationSyncServers),
            ps -> ps.setLong(1, locationId), this::buildStaffLocation);
    }

    public void delete(int locationId) {
        query.create().deleteQuery("DELETE FROM sp_staff_locations WHERE ID = ?", delete -> delete.setInt(1, locationId));
    }
}

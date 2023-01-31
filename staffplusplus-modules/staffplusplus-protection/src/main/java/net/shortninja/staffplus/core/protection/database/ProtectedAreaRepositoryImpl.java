package net.shortninja.staffplus.core.protection.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.location.LocationRepository;
import net.shortninja.staffplus.core.protection.ProtectedArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@IocBean
public class ProtectedAreaRepositoryImpl implements ProtectedAreaRepository {

    private final LocationRepository locationRepository;
    private final String serverNameFilter;
    private final QueryBuilderFactory query;

    public ProtectedAreaRepositoryImpl(LocationRepository locationRepository, Options options, QueryBuilderFactory query) {
        this.locationRepository = locationRepository;
        this.serverNameFilter = "(l1.server_name is null OR l1.server_name='" + options.serverName + "')";
        this.query = query;
    }

    @Override
    public void addProtectedArea(Player protector, ProtectedArea protectedArea) {
        int location1Id = locationRepository.addLocation(protectedArea.getCornerPoint1());
        int location2Id = locationRepository.addLocation(protectedArea.getCornerPoint2());
        query.create().insertQuery("INSERT INTO sp_protected_areas(name, corner_location_1_id, corner_location_2_id, protected_by) " +
                "VALUES(?, ?, ?, ?);",
            (insert) -> {
                insert.setString(1, protectedArea.getName());
                insert.setInt(2, location1Id);
                insert.setInt(3, location2Id);
                insert.setString(4, protector.getUniqueId().toString());
            });
    }

    @Override
    public List<ProtectedArea> getProtectedAreas() {
        return query.create().find("SELECT * FROM sp_protected_areas pa INNER JOIN sp_locations l1 on pa.corner_location_1_id = l1.id INNER JOIN  " +
                " sp_locations l2 ON l2.id = pa.corner_location_2_id WHERE " + serverNameFilter,
            this::buildProtectedArea);
    }

    @Override
    public List<ProtectedArea> getProtectedAreasPaginated(int offset, int amount) {
        return query.create().find("SELECT * FROM sp_protected_areas pa INNER JOIN sp_locations l1 on pa.corner_location_1_id = l1.id INNER JOIN  " +
            " sp_locations l2 ON l2.id = pa.corner_location_2_id WHERE " + serverNameFilter + " LIMIT ?,?", (ps) -> {
            ps.setInt(1, offset);
            ps.setInt(2, amount);
        }, this::buildProtectedArea);
    }

    @Override
    public Optional<ProtectedArea> findById(int id) {
        return query.create().findOne("SELECT * FROM sp_protected_areas pa INNER JOIN sp_locations l1 on pa.corner_location_1_id = l1.id INNER JOIN  " +
            " sp_locations l2 ON l2.id = pa.corner_location_2_id WHERE pa.ID = ?", (ps) -> ps.setInt(1, id), this::buildProtectedArea);
    }

    @Override
    public Optional<ProtectedArea> findByName(String name) {
        return query.create().findOne("SELECT * FROM sp_protected_areas pa INNER JOIN sp_locations l1 on pa.corner_location_1_id = l1.id INNER JOIN  " +
                " sp_locations l2 ON l2.id = pa.corner_location_2_id WHERE name = ? AND " + serverNameFilter,
            (ps) -> ps.setString(1, name),
            this::buildProtectedArea);
    }

    @Override
    public void deleteProtectedArea(int id) {
        query.create().deleteQuery("DELETE FROM sp_protected_areas WHERE ID = ?", (insert) -> insert.setInt(1, id));
    }

    private ProtectedArea buildProtectedArea(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String name = rs.getString(2);

        double location1X = rs.getDouble(7);
        double location1Y = rs.getDouble(8);
        double location1Z = rs.getDouble(9);
        World location1World = Bukkit.getServer().getWorld(rs.getString(10));

        double location2X = rs.getDouble(13);
        double location2Y = rs.getDouble(14);
        double location2Z = rs.getDouble(15);
        World location2World = Bukkit.getServer().getWorld(rs.getString(16));

        Location cornerPointLocation1 = new Location(location1World, location1X, location1Y, location1Z);
        Location cornerPointLocation2 = new Location(location2World, location2X, location2Y, location2Z);

        return new ProtectedArea(id, name, cornerPointLocation1, cornerPointLocation2, UUID.fromString(rs.getString(5)));
    }
}

package net.shortninja.staffplus.core.domain.staff.protect.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.location.LocationRepository;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSqlProtectedAreaRepository implements ProtectedAreaRepository {

    private final LocationRepository locationRepository;
    private final SqlConnectionProvider sqlConnectionProvider;
    private final String serverNameFilter;

    public AbstractSqlProtectedAreaRepository(LocationRepository locationRepository, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.locationRepository = locationRepository;
        this.sqlConnectionProvider = sqlConnectionProvider;
        serverNameFilter = "(l1.server_name is null OR l1.server_name='" + options.serverName + "')";
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public void addProtectedArea(Player protector, ProtectedArea protectedArea) {
        int location1Id = locationRepository.addLocation(protectedArea.getCornerPoint1());
        int location2Id = locationRepository.addLocation(protectedArea.getCornerPoint2());
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_protected_areas(name, corner_location_1_id, corner_location_2_id, protected_by) " +
                 "VALUES(?, ?, ?, ?);")) {
            insert.setString(1, protectedArea.getName());
            insert.setInt(2, location1Id);
            insert.setInt(3, location2Id);
            insert.setString(4, protector.getUniqueId().toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<ProtectedArea> getProtectedAreas() {
        List<ProtectedArea> protectedAreas = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_protected_areas pa INNER JOIN sp_locations l1 on pa.corner_location_1_id = l1.id INNER JOIN  " +
                 " sp_locations l2 ON l2.id = pa.corner_location_2_id WHERE " + serverNameFilter)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    protectedAreas.add(buildProtectedArea(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return protectedAreas;
    }

    @Override
    public List<ProtectedArea> getProtectedAreasPaginated(int offset, int amount) {
        List<ProtectedArea> protectedAreas = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_protected_areas pa INNER JOIN sp_locations l1 on pa.corner_location_1_id = l1.id INNER JOIN  " +
                 " sp_locations l2 ON l2.id = pa.corner_location_2_id WHERE " + serverNameFilter + " LIMIT ?,?")) {
            ps.setInt(1, offset);
            ps.setInt(2, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    protectedAreas.add(buildProtectedArea(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return protectedAreas;
    }

    @Override
    public Optional<ProtectedArea> findById(int id) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_protected_areas pa INNER JOIN sp_locations l1 on pa.corner_location_1_id = l1.id INNER JOIN  " +
                 " sp_locations l2 ON l2.id = pa.corner_location_2_id WHERE pa.ID = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildProtectedArea(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ProtectedArea> findByName(String name) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_protected_areas pa INNER JOIN sp_locations l1 on pa.corner_location_1_id = l1.id INNER JOIN  " +
                 " sp_locations l2 ON l2.id = pa.corner_location_2_id WHERE name = ? AND " + serverNameFilter)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildProtectedArea(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteProtectedArea(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_protected_areas WHERE ID = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private ProtectedArea buildProtectedArea(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String name = rs.getString(2);

        double location1X = rs.getDouble(7);
        double location1Y = rs.getDouble(8);
        double location1Z = rs.getDouble(9);
        World location1World = Bukkit.getServer().getWorld(rs.getString(10));

        double location2X = rs.getDouble(12);
        double location2Y = rs.getDouble(13);
        double location2Z = rs.getDouble(14);
        World location2World = Bukkit.getServer().getWorld(rs.getString(15));

        Location cornerPointLocation1 = new Location(location1World, location1X, location1Y, location1Z);
        Location cornerPointLocation2 = new Location(location2World, location2X, location2Y, location2Z);

        return new ProtectedArea(id, name, cornerPointLocation1, cornerPointLocation2, UUID.fromString(rs.getString(5)));
    }

}

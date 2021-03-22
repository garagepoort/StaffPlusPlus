package net.shortninja.staffplus.core.domain.location;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import org.bukkit.Location;

import java.sql.*;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlLocationRepository extends AbstractSqlLocationRepository {

    public MysqlLocationRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        super(options, sqlConnectionProvider);
    }

    @Override
    public int addLocation(Location location) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_locations(x, y, z, world, server_name) " +
                 "VALUES(?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setInt(1, location.getBlockX());
            insert.setInt(2, location.getBlockY());
            insert.setInt(3, location.getBlockZ());
            insert.setString(4, location.getWorld().getName());
            insert.setString(5, options.serverName);
            insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

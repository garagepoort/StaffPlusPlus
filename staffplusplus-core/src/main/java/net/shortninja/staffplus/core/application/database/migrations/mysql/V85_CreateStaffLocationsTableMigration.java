package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V85_CreateStaffLocationsTableMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        String staffLocations = "CREATE TABLE IF NOT EXISTS sp_staff_locations (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "name VARCHAR(255) NOT NULL, " +
            "location_id INT NOT NULL, " +
            "creator_uuid VARCHAR(36) NOT NULL, " +
            "creator_name VARCHAR(36) NOT NULL, " +
            "server_name VARCHAR(255) NOT NULL, " +
            "creation_timestamp BIGINT NOT NULL, " +
            "FOREIGN KEY (location_id) REFERENCES sp_locations(id) ON DELETE CASCADE, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
        String notes = "CREATE TABLE IF NOT EXISTS sp_staff_location_notes (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "staff_location_id INT NOT NULL,  " +
            "note TEXT NOT NULL,  " +
            "noted_by_uuid VARCHAR(36) NOT NULL,  " +
            "noted_by_name VARCHAR(36) NOT NULL,  " +
            "timestamp BIGINT NOT NULL, " +
            "FOREIGN KEY (staff_location_id) REFERENCES sp_staff_locations(id) ON DELETE CASCADE, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
        return Arrays.asList(staffLocations, notes);
    }

    @Override
    public int getVersion() {
        return 85;
    }
}

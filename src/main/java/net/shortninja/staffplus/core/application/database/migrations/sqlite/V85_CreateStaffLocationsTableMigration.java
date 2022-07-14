package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import java.util.Arrays;
import java.util.List;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V85_CreateStaffLocationsTableMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        String staffLocations = "CREATE TABLE IF NOT EXISTS sp_staff_locations (  " +
            "ID integer PRIMARY KEY,  " +
            "name VARCHAR(255) NOT NULL, " +
            "location_id integer NOT NULL, " +
            "creator_uuid VARCHAR(36) NOT NULL, " +
            "creator_name VARCHAR(36) NOT NULL, " +
            "server_name VARCHAR(255) NOT NULL, " +
            "creation_timestamp BIGINT NOT NULL, " +
            "FOREIGN KEY (location_id) REFERENCES sp_locations(id) ON DELETE CASCADE);";
        String notes = "CREATE TABLE IF NOT EXISTS sp_staff_location_notes (  " +
            "ID integer PRIMARY KEY,  " +
            "staff_location_id integer NOT NULL,  " +
            "note TEXT NOT NULL,  " +
            "noted_by_uuid VARCHAR(36) NOT NULL,  " +
            "noted_by_name VARCHAR(36) NOT NULL,  " +
            "timestamp BIGINT NOT NULL, " +
            "FOREIGN KEY (staff_location_id) REFERENCES sp_staff_locations(id) ON DELETE CASCADE" +
            ");";
        return Arrays.asList(staffLocations, notes);
    }

    @Override
    public int getVersion() {
        return 85;
    }
}

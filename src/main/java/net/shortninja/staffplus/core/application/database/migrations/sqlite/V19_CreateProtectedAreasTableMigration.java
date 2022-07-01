package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V19_CreateProtectedAreasTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_protected_areas (  " +
            "ID integer PRIMARY KEY,  " +
            "name VARCHAR(128) NOT NULL,  " +
            "corner_location_1_id integer NOT NULL,  " +
            "corner_location_2_id integer NOT NULL,  " +
            "protected_by VARCHAR(36) NOT NULL,  " +
            "FOREIGN KEY (corner_location_1_id) REFERENCES sp_locations(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (corner_location_2_id) REFERENCES sp_locations(id) ON DELETE CASCADE)";
    }

    @Override
    public int getVersion() {
        return 19;
    }
}

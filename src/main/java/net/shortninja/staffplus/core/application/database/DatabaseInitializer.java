package net.shortninja.staffplus.core.application.database;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlMigrations;

@IocBean
public class DatabaseInitializer {

    public DatabaseInitializer(SqlMigrations sqlMigrations) {
        sqlMigrations.createMigrationTable();
        sqlMigrations.runMigrations();
    }

}

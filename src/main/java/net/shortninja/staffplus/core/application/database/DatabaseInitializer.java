package net.shortninja.staffplus.core.application.database;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlMigrations;

@IocBean
public class DatabaseInitializer {

    public DatabaseInitializer(SqlMigrations sqlMigrations) {
        sqlMigrations.createMigrationTable();
        sqlMigrations.runMigrations();
    }

}

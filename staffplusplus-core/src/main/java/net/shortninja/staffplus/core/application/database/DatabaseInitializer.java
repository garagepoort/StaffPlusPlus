package net.shortninja.staffplus.core.application.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcsqlmigrations.Migrations;
import be.garagepoort.mcsqlmigrations.MigrationsProvider;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;

@IocBean(priority = true)
public class DatabaseInitializer {

    public DatabaseInitializer(SqlConnectionProvider sqlConnectionProvider,
                               MigrationsProvider migrationsProvider,
                               @ConfigProperty("storage.migrations-table-name") String migrationsTable) {
        new Migrations(sqlConnectionProvider, migrationsProvider).run(migrationsTable);
    }

}

package be.garagepoort.staffplusplus.trello.repository.database;

import be.garagepoort.staffplusplus.trello.repository.database.migrations.SqlMigrations;
import be.garagepoort.staffplusplus.trello.repository.database.migrations.sqlite.SqLiteMigrations;

public class DatabaseInitializer {

    public void initialize() {
        SqlMigrations sqlMigrations = getMigrationHandler();
        sqlMigrations.createMigrationTable();
        sqlMigrations.runMigrations();
    }

    private SqlMigrations getMigrationHandler() {
        return SqLiteMigrations.getInstance();
    }
}

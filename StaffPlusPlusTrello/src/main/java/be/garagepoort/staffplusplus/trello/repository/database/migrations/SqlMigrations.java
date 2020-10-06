package be.garagepoort.staffplusplus.trello.repository.database.migrations;

public interface SqlMigrations {
    void createMigrationTable();

    void runMigrations();
}

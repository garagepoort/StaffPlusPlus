package net.shortninja.staffplus.application.database.migrations;

public interface SqlMigrations {
    void createMigrationTable();

    void runMigrations();
}

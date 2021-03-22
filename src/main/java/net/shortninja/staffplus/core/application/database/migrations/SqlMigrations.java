package net.shortninja.staffplus.core.application.database.migrations;

public interface SqlMigrations {
    void createMigrationTable();

    void runMigrations();
}

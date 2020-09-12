package net.shortninja.staffplus.util.database.migrations;

public interface SqlMigrations {
    void createMigrationTable();

    void runMigrations();
}

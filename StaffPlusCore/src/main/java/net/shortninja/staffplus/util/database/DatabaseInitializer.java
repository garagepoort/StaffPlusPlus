package net.shortninja.staffplus.util.database;

import net.shortninja.staffplus.util.database.migrations.SqlMigrations;
import net.shortninja.staffplus.util.database.migrations.mysql.MysqlMigrations;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqLiteMigrations;

public class DatabaseInitializer {

    public void initialize() {
            DatabaseType type = DatabaseUtil.database().getType();
            DatabaseUtil.database().init();

            SqlMigrations sqlMigrations = getMigrationHandler(type);
            sqlMigrations.createMigrationTable();
            sqlMigrations.runMigrations();
    }

    private SqlMigrations getMigrationHandler(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return MysqlMigrations.getInstance();
            case SQLITE:
                return SqLiteMigrations.getInstance();
            default:
                throw new RuntimeException("Invalid database type provided: " + type);
        }
    }
}

package net.shortninja.staffplus.application.database;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;
import net.shortninja.staffplus.common.config.Options;

public class DatabaseUtil {

    private static DatabaseUtil instance;
    private static final String MYSQL_STRING = "MYSQL";
    private static final String SQLITE_STRING = "SQLITE";
    private Options options = IocContainer.getOptions();

    private DatabaseType databaseType;

    public static DatabaseUtil database() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    private DatabaseUtil() {
        String databaseType = options.storageType;
        if (MYSQL_STRING.equalsIgnoreCase(databaseType)) {
            this.databaseType = DatabaseType.MYSQL;
        } else if (SQLITE_STRING.equalsIgnoreCase(databaseType)) {
            this.databaseType = DatabaseType.SQLITE;
        } else {
            throw new RuntimeException("Invalid database type configured: " + databaseType);
        }
    }

    public void init() {
        if (databaseType == DatabaseType.MYSQL) {
            MySQLConnection.getInstance().initDataSource();
        } else if (databaseType == DatabaseType.SQLITE) {
            //No initialization needed
        } else {
            throw new RuntimeException("No database configured");
        }
    }

    public DatabaseType getType() {
        return databaseType;
    }
}

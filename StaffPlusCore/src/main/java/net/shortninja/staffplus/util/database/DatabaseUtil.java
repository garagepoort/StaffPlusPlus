package net.shortninja.staffplus.util.database;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

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

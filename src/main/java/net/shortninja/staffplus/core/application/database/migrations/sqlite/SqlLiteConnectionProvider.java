package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.load.InjectTubingPlugin;
import be.garagepoort.mcsqlmigrations.DatabaseType;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqlLiteConnectionProvider implements SqlConnectionProvider {

    private static final Object LOCK = new Object();
    private final StaffPlusPlus staffPlusPlus;
    private Connection connection;

    public SqlLiteConnectionProvider(@InjectTubingPlugin StaffPlusPlus staffPlusPlus) {
        this.staffPlusPlus = staffPlusPlus;
    }

    @Override
    public Connection getConnection() {
    
        synchronized (LOCK) {
            String url = "jdbc:sqlite:plugins/StaffPlusPlus/staff.db";
            try {
                Class.forName("org.sqlite.JDBC")
                long totalWaitTime = 0;
                while (connection != null && !connection.isClosed()) {
                    Thread.sleep(1);
                    totalWaitTime += 1;
                    if (totalWaitTime > 3000) {
                        connection.close();
                        break;
                    }
                }

                connection = DriverManager.getConnection(url);
                return connection;
            } catch (SQLException | InterruptedException | ClassNotFoundException e) {
                throw new DatabaseException("Failed to connect to the database", e);
            }
        }
    }

    @Override
    public DataSource getDatasource() {
        return null;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.SQLITE;
    }
}

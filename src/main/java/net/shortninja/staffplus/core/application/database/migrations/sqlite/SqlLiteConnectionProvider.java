package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.DatabaseType;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqlLiteConnectionProvider implements SqlConnectionProvider {

    @Override
    public Connection getConnection() {
        String url = "jdbc:sqlite:plugins/StaffPlus/staff.db";
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to the database", e);
        }
    }

    @Override
    public DataSource getDatasource() {
        return null;
    }

    @Override
    public List<String> getMigrationPackages() {
        return Arrays.asList("net.shortninja.staffplus.core.application.database.migrations.sqlite", "net.shortninja.staffplus.core.application.database.migrations.common");
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.SQLITE;
    }
}

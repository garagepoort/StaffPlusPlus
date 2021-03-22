package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}

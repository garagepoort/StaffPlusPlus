package net.shortninja.staffplus.application.database.migrations.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlLiteConnection {

    public static Connection connect() throws SQLException {
        String url = "jdbc:sqlite:plugins/StaffPlus/staff.db";
        return DriverManager.getConnection(url);
    }
}

package be.garagepoort.staffplusplus.trello.repository.database.migrations.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlLiteConnection {

    public static Connection connect() throws SQLException {
        String url = "jdbc:sqlite:plugins/StaffPlusPlusTrello/staffPlusPlusTrello.db";
        return DriverManager.getConnection(url);
    }
}

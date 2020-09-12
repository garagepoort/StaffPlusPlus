package net.shortninja.staffplus.util.database.migrations.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlLiteConnection {

    public static Connection connect() throws SQLException {
        String url = "jdbc:sqlite:Plugins/TradeSystem/tradelog.db";
        return DriverManager.getConnection(url);
    }
}

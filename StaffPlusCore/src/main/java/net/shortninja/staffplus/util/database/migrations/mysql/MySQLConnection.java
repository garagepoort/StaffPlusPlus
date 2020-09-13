package net.shortninja.staffplus.util.database.migrations.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnection {

    private static MySQLConnection instance;
    private static HikariDataSource datasource;
    private static Options options = StaffPlus.get().options;

    public static MySQLConnection getInstance() {
        if(instance == null) {
            instance = new MySQLConnection();
        }
        return instance;
    }

    public DataSource initDataSource() {
        getDataSource();
        return datasource;
    }

    public static DataSource getDatasource() {
        if(datasource == null){
            getDataSource();
        }
        return datasource;
    }

    public static Connection getConnection() throws SQLException {
        return getDatasource().getConnection();
    }

    private static void getDataSource() {
        if(datasource == null) {
            HikariConfig config = new HikariConfig();
            String host = options.mySqlHost;
            int port = options.mySqlPort;
            String db = options.database;
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + db + "?autoReconnect=true&useSSL=false");
            config.setUsername(options.mySqlUser);
            config.setPassword(options.mySqlPassword);
            config.setMaximumPoolSize(5);
            config.setLeakDetectionThreshold(2000);
            config.setAutoCommit(true);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            datasource = new HikariDataSource(config);
        }
    }
}

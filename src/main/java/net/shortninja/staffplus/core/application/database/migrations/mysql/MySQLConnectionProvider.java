package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcsqlmigrations.DatabaseType;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@ConfigProperties("storage.mysql")
public class MySQLConnectionProvider implements SqlConnectionProvider {

    @ConfigProperty("host")
    private String host;
    @ConfigProperty("user")
    private String user;
    @ConfigProperty("database")
    private String database;
    @ConfigProperty("password")
    private String password;
    @ConfigProperty("port")
    private int port;
    @ConfigProperty("ssl-enabled")
    private boolean sslEnabled;

    @ConfigProperty("max-pool-size")
    private int maxPoolSize;

    private HikariDataSource datasource;

    public DataSource getDatasource() {
        if (datasource == null) {
            getDataSource();
        }
        return datasource;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return getDatasource().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DatabaseException("Failed to connect to the database", e);
        }
    }

    private void getDataSource() {
        if (datasource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=" + sslEnabled + "&allowMultiQueries=true&allowPublicKeyRetrieval=true");
            config.setUsername(user);
            config.setPassword(password);
            config.setMaximumPoolSize(maxPoolSize);
            config.setLeakDetectionThreshold(5000);
            config.setAutoCommit(true);
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            datasource = new HikariDataSource(config);
        }
    }
}

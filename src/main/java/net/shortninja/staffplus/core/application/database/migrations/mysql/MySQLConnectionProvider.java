package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.DatabaseType;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MySQLConnectionProvider implements SqlConnectionProvider {

    private HikariDataSource datasource;
    private final Options options;

    public MySQLConnectionProvider(Options options) {
        this.options = options;
        getDataSource();
    }

    public DataSource getDatasource() {
        if(datasource == null){
            getDataSource();
        }
        return datasource;
    }

    @Override
    public List<String> getMigrationPackages() {
        return Arrays.asList("net.shortninja.staffplus.core.application.database.migrations.mysql", "net.shortninja.staffplus.core.application.database.migrations.common");
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }

    public Connection getConnection() {
        try {
            return getDatasource().getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to the database", e);
        }
    }

    private void getDataSource() {
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

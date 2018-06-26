package net.shortninja.staffplus.server.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLConnection {

    private static HikariDataSource datasource;
    private static Connection connection;
    private Options options = StaffPlus.get().options;

    private HikariDataSource getDataSource() {
        if(datasource == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            HikariConfig config = new HikariConfig();
            String host = options.mySqlHost;
            int port = options.mySqlPort;
            String db = options.database;
            config.setJdbcUrl("jdbc:mysql://"+host+":"+port+"/"+db);
            config.setUsername(options.mySqlUser);
            config.setPassword(options.mySqlPassword);
            config.setMaximumPoolSize(10);
            config.setAutoCommit(false);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            datasource = new HikariDataSource(config);
        }
        return datasource;
    }

    public boolean init() {
        getDataSource();
        try {
            StaffPlus.get().getLogger().info("Connection established with the database!");
            PreparedStatement pr = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_reports` (  `Number` INT NOT NULL AUTO_INCREMENT,  `Reason` VARCHAR(255) NULL,  `Reporter_UUID` VARCHAR(36) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`Number`)) ENGINE = InnoDB;");
            PreparedStatement pw = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_warnings` (`Number` INT NOT NULL AUTO_INCREMENT,  `Reason` VARCHAR(255) NULL,  `Warner_UUID` VARCHAR(36) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`Number`)) ENGINE = InnoDB;");
            PreparedStatement ao = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_alert_options` ( `Name_Change` VARCHAR(5) NULL,  `Mention` VARCHAR(5) NULL,  `Xray` VARCHAR(5) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`Player_UUID`)) ENGINE = InnoDB;");
            PreparedStatement pd = getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `sp_playerdata` ( `GlassColor` VARCHAR(45) NULL, `Password` VARCHAR(255) NULL, `Warnings_Number` INT NOT NULL, `Reports_Number` INT NOT NULL, `Player_UUID` VARCHAR(36) NOT NULL," +
                            "  INDEX `fk_playerdata_Warnings_idx` (`Warnings_Number` ASC), INDEX `fk_playerdata_Reports_idx` (`Reports_Number` ASC), CONSTRAINT pk_sp_playerdata PRIMARY KEY (`Player_UUID`)," + "  CONSTRAINT `fk_playerdata_Warnings`   FOREIGN KEY (`Warnings_Number`)   REFERENCES `sp_warnings` (`Number`)   " +
                            "ON DELETE NO ACTION   ON UPDATE NO ACTION, CONSTRAINT `fk_playerdata_Reports`   FOREIGN KEY (`Reports_Number`)" + "    REFERENCES `sp_reports` (`Number`)   ON DELETE NO ACTION" + "   ON UPDATE NO ACTION, CONSTRAINT `fk_playerdata_alert_options`   FOREIGN KEY (`AlertOptions_Player_UUID`)   " +
                            "REFERENCES `sp_alert_options` (`Player_UUID`)   ON DELETE NO ACTION ON UPDATE NO ACTION)ENGINE = InnoDB;");
            ao.executeUpdate();
            ao.close();
            pw.executeUpdate();
            pw.close();
            pr.executeUpdate();
            pr.close();
            pd.executeUpdate();
            pd.close();
            return true;
        } catch (SQLException e) {
            StaffPlus.get().getLogger().info("Connection failed with the database! Details correct?");
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection()throws SQLException{
            return datasource.getConnection();
    }

    public static void kill() {
        datasource.close();
    }
}

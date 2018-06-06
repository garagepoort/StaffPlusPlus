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
            PreparedStatement pr = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_reports` (  `Number` INT NOT NULL,  `Reason` VARCHAR(255) NULL,  `ReporterUUID` VARCHAR(36) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`Number`, `Player_UUID`)) ENGINE = InnoDB;");
            PreparedStatement pw = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_warnings` (`Number` INT NOT NULL,  `Reason` VARCHAR(255) NULL,  `Warner_UUID` VARCHAR(36) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`Number`, `Player_UUID`)) ENGINE = InnoDB;");
            PreparedStatement ao = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_alert_options` ( `NameChange` VARCHAR(5) NULL,  `Mention` VARCHAR(5) NULL,  `Xray` VARCHAR(5) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`Player_UUID`)) ENGINE = InnoDB;");
            PreparedStatement pd = getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `sp_playerdata` ( `GlassColor` VARCHAR(45) NULL,\n" +
                            "  `Password` VARCHAR(255) NULL,\n" +
                            "  `Warnings_Number` INT NOT NULL,\n" +
                            "  `Reports_Number` INT NOT NULL,\n" +
                            "  `Player_UUID` VARCHAR(36) NOT NULL,\n" +
                            "  `Alert_Options_Player_UUID` VARCHAR(36) NOT NULL,\n" +
                            "  INDEX `fk_PlayerData_Warnings1_idx` (`warnings_Number` ASC),\n" +
                            "  INDEX `fk_PlayerData_Reports1_idx` (`reports_Number` ASC),\n" +
                            "  PRIMARY KEY (`Player_UUID`, `alert_options_Player_UUID`),\n" +
                            "  INDEX `fk_PlayerData_alert_options1_idx` (`alert_options_Player_UUID` ASC),\n" +
                            "  CONSTRAINT `fk_PlayerData_Warnings1`\n" +
                            "    FOREIGN KEY (`warnings_Number`)\n" +
                            "    REFERENCES `sp_warnings` (`Number`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION,\n" +
                            "  CONSTRAINT `fk_PlayerData_Reports1`\n" +
                            "    FOREIGN KEY (`reports_Number`)\n" +
                            "    REFERENCES `sp_reports` (`Number`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION,\n" +
                            "  CONSTRAINT `fk_PlayerData_AlertOptions1`\n" +
                            "    FOREIGN KEY (`alert_options_Player_UUID`)\n" +
                            "    REFERENCES `sp_alert_options` (`Player_UUID`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION)ENGINE = InnoDB;");
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

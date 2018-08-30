package net.shortninja.staffplus.server.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLConnection {

    private static HikariDataSource datasource;
    private Options options = StaffPlus.get().options;


    private  HikariDataSource getDataSource() {
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
            config.setJdbcUrl("jdbc:mysql://"+host+":"+port+"/"+db+"?autoReconnect=true&useSSL=false");
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
            PreparedStatement pr = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_reports` (  `ID` INT NOT NULL AUTO_INCREMENT,  `Reason` VARCHAR(255) NULL,  `Reporter_UUID` VARCHAR(36) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
            PreparedStatement pw = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_warnings` (`ID` INT NOT NULL AUTO_INCREMENT,  `Reason` VARCHAR(255) NULL,  `Warner_UUID` VARCHAR(36) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
            PreparedStatement ao = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `sp_alert_options` ( `Name_Change` VARCHAR(5) NULL,  `Mention` VARCHAR(5) NULL,  `Xray` VARCHAR(5) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`Player_UUID`)) ENGINE = InnoDB;");
            PreparedStatement pd = getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `sp_playerdata` ( `GlassColor` INT NULL, `Password` VARCHAR(255) NOT NULL, `Player_UUID` VARCHAR(36) NOT NULL, `Name` VARCHAR(18) NOT NULL, PRIMARY KEY (`Player_UUID`))  ENGINE = InnoDB;");
            PreparedStatement tickets = getConnection().prepareCall("CREATE TABLE IF NOT EXISTS `sp_tickets` ( `UUID` VARCHAR(36) NOT NULL, `ID` INT NOT NULL, `Inquiry` VARCHAR(255) NOT NULL, PRIMARY KEY (`UUID`)) ENGINE = InnoDB;");
            PreparedStatement commands = getConnection().prepareCall("CREATE TABLE IF NOT EXISTS `sp_commands` (`Command_Name` VARCHAR(36) NOT NULL, `Command` VARCHAR(36) NOT NULL, PRIMARY KEY (`Command_Name`)) ENGINE = InnoDB");
            commands.executeUpdate();
            commands.close();
            tickets.executeUpdate();
            tickets.close();
            ao.executeUpdate();
            ao.close();
            pw.executeUpdate();
            pw.close();
            pr.executeUpdate();
            pr.close();
            pd.executeUpdate();
            pd.close();
            importData();
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

    private void importData(){
        FileConfiguration save = StaffPlus.get().dataFile.getConfiguration();
        if(!StaffPlus.get().getConfig().getBoolean("storage.mysql.migrated"))
        {
            try {
                for (String key : save.getConfigurationSection("").getKeys(false)) {
                    PreparedStatement pd = getConnection().prepareStatement("INSERT INTO sp_playerdata(GlassColor, Password, Player_UUID, Name)" +
                            "VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE Player_UUID=?;");
                    pd.setInt(1,save.getInt(key+".glass-color"));
                    pd.setString(2,save.getString(key+".password"));
                    pd.setString(3,key);
                    pd.setString(4,save.getString(key+".name"));
                    pd.setString(5,key);
                    pd.executeUpdate();
                    pd.close();
                    for(String reportInfo : save.getStringList(key+".reports")){
                        String[] info = reportInfo.split(";");
                        PreparedStatement report = getConnection().prepareStatement("INSERT INTO sp_reports(Reason, Reporter_UUID, Player_UUID) " +
                                "VALUES(?, ?, ?);");
                        report.setString(1,info[0]);
                        report.setString(2,info[2]);
                        report.setString(3,key);
                        report.executeUpdate();
                        report.close();
                    }
                    for(String warnInfo : save.getStringList(key+".warnings")){
                        String[] info = warnInfo.split(";");
                        PreparedStatement warn = getConnection().prepareStatement("INSERT INTO sp_warnings(Reason, Warner_UUID, Player_UUID) " +
                                "VALUES(?, ?, ?);");
                        warn.setString(1,info[0]);
                        warn.setString(2,info[2]);
                        warn.setString(3,key);
                        warn.executeUpdate();
                        warn.close();
                    }
                    for(String alertOptions : save.getStringList(key+".alert-options")){
                        String[] info = alertOptions.split(";");
                        if(info[0].equalsIgnoreCase("name_change")) {
                            PreparedStatement alert = getConnection().prepareStatement("INSERT INTO sp_alert_options(Name_Change, Player_UUID) " +
                                    "VALUES(?, ?) ON DUPLICATE KEY UPDATE Name_Change=?;");
                            alert.setString(1,info[1]);
                            alert.setString(2,key);
                            alert.setString(3, info[1]);
                            alert.executeUpdate();
                            alert.close();
                        }else if(info[0].equalsIgnoreCase("xray")){
                            PreparedStatement alert = getConnection().prepareStatement("INSERT INTO sp_alert_options(Xray, Player_UUID) " +
                                    "VALUES(?, ?) ON DUPLICATE KEY UPDATE Xray=?;");
                            alert.setString(1,info[1]);
                            alert.setString(2,key);
                            alert.setString(3, info[1]);
                            alert.executeUpdate();
                            alert.close();
                        }else if(info[0].equalsIgnoreCase("mention")){
                            PreparedStatement alert = getConnection().prepareStatement("INSERT INTO sp_alert_options(Mention, Player_UUID) " +
                                    "VALUES(?, ?) ON DUPLICATE KEY UPDATE Mention=?;");
                            alert.setString(1,info[1]);
                            alert.setString(2,key);
                            alert.setString(3, info[1]);
                            alert.executeUpdate();
                            alert.close();
                        }
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            StaffPlus.get().getConfig().set("storage.mysql.migrated",true);
        }
    }
}

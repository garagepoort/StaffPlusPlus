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
            config.setMaximumPoolSize(20);
            config.setIdleTimeout(30000);
            config.setLeakDetectionThreshold(2000);
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
            Connection connection = getConnection();
            StaffPlus.get().getLogger().info("Connection established with the database!");
            PreparedStatement pr = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `sp_reports` (  `ID` INT NOT NULL AUTO_INCREMENT,  `Reason` VARCHAR(255) NULL,  `Reporter_UUID` VARCHAR(36) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
            PreparedStatement pw = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `sp_warnings` (`ID` INT NOT NULL AUTO_INCREMENT,  `Reason` VARCHAR(255) NULL,  `Warner_UUID` VARCHAR(36) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
            PreparedStatement ao = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `sp_alert_options` ( `Name_Change` VARCHAR(5) NULL,  `Mention` VARCHAR(5) NULL,  `Xray` VARCHAR(5) NULL,  `Player_UUID` VARCHAR(36) NOT NULL,  PRIMARY KEY (`Player_UUID`)) ENGINE = InnoDB;");
            PreparedStatement pd = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `sp_playerdata` ( `GlassColor` INT NULL, `Password` VARCHAR(255) NOT NULL, `Player_UUID` VARCHAR(36) NOT NULL, `Name` VARCHAR(18) NOT NULL, PRIMARY KEY (`Player_UUID`))  ENGINE = InnoDB;");
            PreparedStatement tickets = connection.prepareCall("CREATE TABLE IF NOT EXISTS `sp_tickets` ( `UUID` VARCHAR(36) NOT NULL, `ID` INT NOT NULL, `Inquiry` VARCHAR(255) NOT NULL, PRIMARY KEY (`UUID`)) ENGINE = InnoDB;");
            PreparedStatement commands = connection.prepareCall("CREATE TABLE IF NOT EXISTS `sp_commands` (`Command_Name` VARCHAR(36) NOT NULL, `Command` VARCHAR(36) NOT NULL, PRIMARY KEY (`Command_Name`)) ENGINE = InnoDB");
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
            connection.close();
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
            Connection connection = null;
            PreparedStatement pd = null;
            PreparedStatement report = null;
            PreparedStatement warn = null;
            PreparedStatement name = null;
            PreparedStatement xray = null;
            PreparedStatement mention = null;
            try {
                connection = getConnection();
                pd = connection.prepareStatement("INSERT INTO sp_playerdata(GlassColor, Password, Player_UUID, Name)" +
                        "VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE Player_UUID=?;");
                report = connection.prepareStatement("INSERT INTO sp_reports(Reason, Reporter_UUID, Player_UUID) " +
                        "VALUES(?, ?, ?);");
                warn = connection.prepareStatement("INSERT INTO sp_warnings(Reason, Warner_UUID, Player_UUID) " +
                        "VALUES(?, ?, ?);");
                name =  connection.prepareStatement("INSERT INTO sp_alert_options(Name_Change, Player_UUID) " +
                        "VALUES(?, ?) ON DUPLICATE KEY UPDATE Name_Change=?;");
                xray= connection.prepareStatement("INSERT INTO sp_alert_options(Xray, Player_UUID) " +
                        "VALUES(?, ?) ON DUPLICATE KEY UPDATE Xray=?;");
                mention = connection.prepareStatement("INSERT INTO sp_alert_options(Mention, Player_UUID) " +
                        "VALUES(?, ?) ON DUPLICATE KEY UPDATE Mention=?;");
                for (String key : save.getConfigurationSection("").getKeys(false)) {
                    pd.setInt(1,save.getInt(key+".glass-color"));
                    pd.setString(2,save.getString(key+".password"));
                    pd.setString(3,key);
                    pd.setString(4,save.getString(key+".name"));
                    pd.setString(5,key);
                    pd.executeUpdate();
                    for(String reportInfo : save.getStringList(key+".reports")){
                        String[] info = reportInfo.split(";");
                        report.setString(1,info[0]);
                        report.setString(2,info[2]);
                        report.setString(3,key);
                        report.executeUpdate();
                    }
                    for(String warnInfo : save.getStringList(key+".warnings")){
                        String[] info = warnInfo.split(";");
                        warn.setString(1,info[0]);
                        warn.setString(2,info[2]);
                        warn.setString(3,key);
                        warn.executeUpdate();
                    }
                    for(String alertOptions : save.getStringList(key+".alert-options")){
                        String[] info = alertOptions.split(";");
                        if(info[0].equalsIgnoreCase("name_change")) {
                            name.setString(1,info[1]);
                            name.setString(2,key);
                            name.setString(3, info[1]);
                            name.executeUpdate();
                        }else if(info[0].equalsIgnoreCase("xray")){
                            xray.setString(1,info[1]);
                            xray.setString(2,key);
                            xray.setString(3, info[1]);
                            xray.executeUpdate();
                        }else if(info[0].equalsIgnoreCase("mention")){
                            mention.setString(1,info[1]);
                            mention.setString(2,key);
                            mention.setString(3, info[1]);
                            mention.executeUpdate();
                        }
                    }
                }
                pd.close();
                report.close();
                warn.close();
                name.close();
                xray.close();
                mention.close();
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }finally{
               if(connection!=null &&
                       pd != null && report != null && warn !=null &&
                       name !=null && xray !=null && mention !=null){
                   try {
                       pd.close();
                       report.close();
                       warn.close();
                       name.close();
                       xray.close();
                       mention.close();
                       connection.close();
                   } catch (SQLException e) {
                       e.printStackTrace();
                   }
               }
            }
            StaffPlus.get().message.sendConsoleMessage("Data has been imported to MySQL from flatfile",false);
            StaffPlus.get().getConfig().set("storage.mysql.migrated",true);
            StaffPlus.get().saveConfig();
        }
    }
}

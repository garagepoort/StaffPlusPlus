package net.shortninja.staffplus.server.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLConnection {

    private static HikariDataSource datasource;
    private Options options = StaffPlus.get().options;

    private void getDataSource() {
        if(datasource == null) {
            HikariConfig config = new HikariConfig();
            String host = options.mySqlHost;
            int port = options.mySqlPort;
            String db = options.database;
            config.setJdbcUrl("jdbc:mysql://"+host+":"+port+"/"+db+"?autoReconnect=true&useSSL=false");
            config.setUsername(options.mySqlUser);
            config.setPassword(options.mySqlPassword);
            config.setMaximumPoolSize(5);
            config.setLeakDetectionThreshold(2000);
            config.setAutoCommit(false);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            datasource = new HikariDataSource(config);
        }
    }

    public boolean init() {
        getDataSource();
        try(Connection connection = datasource.getConnection();
            PreparedStatement pr = connection.prepareStatement("CREATE TABLE IF NOT EXISTS sp_reports (  ID INT NOT NULL AUTO_INCREMENT,  Reason VARCHAR(255) NULL,  Reporter_UUID VARCHAR(36) NULL,  Player_UUID VARCHAR(36) NOT NULL,  PRIMARY KEY (ID)) ENGINE = InnoDB;");
            PreparedStatement pw = connection.prepareStatement("CREATE TABLE IF NOT EXISTS sp_warnings (ID INT NOT NULL AUTO_INCREMENT,  Reason VARCHAR(255) NULL,  Warner_UUID VARCHAR(36) NULL,  Player_UUID VARCHAR(36) NOT NULL,  PRIMARY KEY (ID)) ENGINE = InnoDB;");
            PreparedStatement ao = connection.prepareStatement("CREATE TABLE IF NOT EXISTS sp_alert_options ( Name_Change VARCHAR(5) NULL,  Mention VARCHAR(5) NULL,  Xray VARCHAR(5) NULL,  Player_UUID VARCHAR(36) NOT NULL,  PRIMARY KEY (Player_UUID)) ENGINE = InnoDB;");
            PreparedStatement pd = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS sp_playerdata ( GlassColor INT NOT NULL DEFAULT 0, Password VARCHAR(255) NOT NULL DEFAULT '', Player_UUID VARCHAR(36) NOT NULL, Name VARCHAR(18) NOT NULL, PRIMARY KEY (Player_UUID))  ENGINE = InnoDB;");
            PreparedStatement tickets = connection.prepareCall("CREATE TABLE IF NOT EXISTS sp_tickets ( UUID VARCHAR(36) NOT NULL, ID INT NOT NULL, Inquiry VARCHAR(255) NOT NULL, PRIMARY KEY (UUID)) ENGINE = InnoDB;");
            PreparedStatement commands = connection.prepareCall("CREATE TABLE IF NOT EXISTS sp_commands (Command_Name VARCHAR(36) NOT NULL, Command VARCHAR(36) NOT NULL, PRIMARY KEY (Command_Name)) ENGINE = InnoDB;");
            PreparedStatement alter = connection.prepareStatement("ALTER TABLE sp_playerdata CHANGE Password Password VARCHAR(255) NOT NULL DEFAULT '';")){;
            StaffPlus.get().getLogger().info("Connection established with the database!");
            commands.executeUpdate();
            tickets.executeUpdate();
            ao.executeUpdate();
            pw.executeUpdate();
            pr.executeUpdate();
            pd.executeUpdate();
            alter.executeUpdate();
            connection.close();
            System.out.println(connection.isClosed());
            importData();
            return true;
        } catch (SQLException e) {
            StaffPlus.get().message.sendConsoleMessage("Connection failed with the database! Are the details correct?",true);
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
                try(Connection connection = getConnection();
                    PreparedStatement pd = connection.prepareStatement("INSERT INTO sp_playerdata " +
                            "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Player_UUID=?");
                    PreparedStatement report = connection.prepareStatement("INSERT INTO sp_reports " +
                            "VALUES(?, ?, ?);");
                    PreparedStatement warn = connection.prepareStatement("INSERT INTO sp_warnings " +
                            "VALUES (?, ?, ?);");
                    PreparedStatement  name =  connection.prepareStatement("INSERT INTO sp_alert_options(Name_Change, Player_UUID) " +
                            "VALUES (?, ?) ON DUPLICATE KEY UPDATE Name_Change=?;");
                    PreparedStatement xray= connection.prepareStatement("INSERT INTO sp_alert_options(Xray, Player_UUID) " +
                            "VALUES (?, ?) ON DUPLICATE KEY UPDATE Xray=?;");
                    PreparedStatement mention = connection.prepareStatement("INSERT INTO sp_alert_options(Mention, Player_UUID) " +
                            "VALUES (?, ?) ON DUPLICATE KEY UPDATE Mention=?;")){
                    for (String key : save.getConfigurationSection("").getKeys(false)) {
                        if(connection.isClosed()){
                            StaffPlus.get().message.sendConsoleMessage("Connection is closed for some reason",true);
                        }
                        StaffPlus.get().message.sendConsoleMessage(key +" "+save.getString(key+".name")+" "+
                                save.getInt(key+".glass-color")+ " "+save.getString(key+".password"),false);
                        pd.setInt(1,save.getInt(key+".glass-color"));
                        pd.setString(2,save.getString(key+".password"));
                        pd.setString(3,key);
                        pd.setString(4,save.getString(key+".name"));
                        pd.setString(5,key);
                        StaffPlus.get().message.sendConsoleMessage("Update returned "+pd.executeUpdate(),false);
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
                }catch (SQLException e){
                    e.printStackTrace();
                }

                StaffPlus.get().message.sendConsoleMessage("Data has been imported to MySQL from flatfile",false);
                //StaffPlus.get().getConfig().set("storage.mysql.migrated",true);

                StaffPlus.get().saveConfig();
        }
    }
}

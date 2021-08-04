package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class V65_AlterPlayerIpsTableMakeIncreaseIpColumnMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.asList(
            createBackupTable(),
            "INSERT INTO sp_player_ips_backup SELECT player_uuid, ip, timestamp, ip_numeric, player_name, server_name FROM sp_player_ips;",
            "DROP TABLE sp_player_ips;",
            createNewTable(),
            "INSERT INTO sp_player_ips SELECT player_uuid, ip, timestamp, ip_numeric, player_name, server_name FROM sp_player_ips_backup;",
            "DROP TABLE sp_player_ips_backup;");
    }

    @NotNull
    private String createNewTable() {
        return "CREATE TABLE sp_player_ips (  player_uuid VARCHAR(36) NOT NULL," +
            "ip VARCHAR(40) NOT NULL," +
            "timestamp BIGINT," +
            "ip_numeric BIGINT," +
            "player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown'," +
            "server_name VARCHAR(255) null," +
            "PRIMARY KEY (player_uuid, ip));" +
            "CREATE INDEX sp_player_ips_ipnumeric ON sp_player_ips_backup(ip_numeric);";
    }

    @NotNull
    private String createBackupTable() {
        return "CREATE TABLE sp_player_ips_backup (" +
            "player_uuid VARCHAR(36) NOT NULL," +
            "ip VARCHAR(15) NOT NULL," +
            "timestamp BIGINT," +
            "ip_numeric BIGINT," +
            "player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown'," +
            "server_name VARCHAR(255) null," +
            "PRIMARY KEY (player_uuid, ip));" +
            "CREATE INDEX sp_player_ips_ipnumeric ON sp_player_ips_backup(ip_numeric);";
    }

    @Override
    public int getVersion() {
        return 65;
    }
}

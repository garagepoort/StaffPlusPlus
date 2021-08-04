package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class V66_AlterBannedPlayerIpsTableIncreaseIpColumnMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.asList(
            createBackupTable(),
            "INSERT INTO sp_banned_ips_backup SELECT ID, ip, issuer_name, issuer_uuid, unbanned_by_uuid, unbanned_by_name, creation_timestamp, server_name, silent_ban, silent_unban, end_timestamp  FROM sp_banned_ips;",
            "DROP TABLE sp_banned_ips;",
            createNewTable(),
            "INSERT INTO sp_banned_ips SELECT ID, ip, issuer_name, issuer_uuid, unbanned_by_uuid, unbanned_by_name, creation_timestamp, server_name, silent_ban, silent_unban, end_timestamp FROM sp_banned_ips_backup;",
            "DROP TABLE sp_banned_ips_backup;");
    }

    @NotNull
    private String createNewTable() {
        return "CREATE TABLE sp_banned_ips (" +
            "ID integer PRIMARY KEY," +
            "ip VARCHAR(40) NOT NULL," +
            "issuer_name VARCHAR(36) NOT NULL," +
            "issuer_uuid VARCHAR(36) NOT NULL," +
            "unbanned_by_uuid VARCHAR(36) NULL," +
            "unbanned_by_name VARCHAR(36) NULL," +
            "creation_timestamp BIGINT NOT NULL," +
            "server_name VARCHAR(255) NULL," +
            "silent_ban boolean NOT NULL," +
            "silent_unban boolean NULL," +
            "end_timestamp BIGINT NULL)";
    }

    @NotNull
    private String createBackupTable() {
        return "CREATE TABLE sp_banned_ips_backup (" +
            "ID integer PRIMARY KEY," +
            "ip VARCHAR(15) NOT NULL," +
            "issuer_name VARCHAR(36) NOT NULL," +
            "issuer_uuid VARCHAR(36) NOT NULL," +
            "unbanned_by_uuid VARCHAR(36) NULL," +
            "unbanned_by_name VARCHAR(36) NULL," +
            "creation_timestamp BIGINT NOT NULL," +
            "server_name VARCHAR(255) NULL," +
            "silent_ban boolean NOT NULL," +
            "silent_unban boolean NULL," +
            "end_timestamp BIGINT NULL)";
    }

    @Override
    public int getVersion() {
        return 66;
    }
}

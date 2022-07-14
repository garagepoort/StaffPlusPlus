package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V84_AlterWarningsTableMakeFixBooleanColumns implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        return Arrays.asList(
            createBackupTable(),
            "INSERT INTO sp_warnings_backup SELECT ID,Reason,Warner_UUID,Player_UUID,score,severity,is_read,timestamp,server_name,is_expired,player_name,warner_name FROM sp_warnings;",
            "DROP TABLE sp_warnings;",
            createNewTable(),
            "INSERT INTO sp_warnings SELECT ID,Reason,Warner_UUID,Player_UUID,score,severity,is_read,timestamp,server_name,is_expired,player_name,warner_name FROM sp_warnings_backup;",
            "DROP TABLE sp_warnings_backup;");
    }

    @NotNull
    private String createNewTable() {
        return "CREATE TABLE sp_warnings " +
            "(ID INTEGER PRIMARY KEY,  " +
            "Reason VARCHAR(255) NULL,  " +
            "Warner_UUID VARCHAR(36) NULL,  " +
            "Player_UUID VARCHAR(36) NOT NULL, " +
            "score SMALLINT NOT NULL DEFAULT 0, " +
            "severity VARCHAR(36), " +
            "is_read boolean NOT NULL DEFAULT 0, " +
            "timestamp BIGINT NOT NULL, " +
            "server_name VARCHAR(255) null, " +
            "is_expired boolean NOT NULL DEFAULT 0, " +
            "player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown', " +
            "warner_name VARCHAR(32) NOT NULL DEFAULT 'Unknown')";
    }

    @NotNull
    private String createBackupTable() {
        return "CREATE TABLE sp_warnings_backup " +
            "(ID INTEGER PRIMARY KEY,  " +
            "Reason VARCHAR(255) NULL,  " +
            "Warner_UUID VARCHAR(36) NULL,  " +
            "Player_UUID VARCHAR(36) NOT NULL, " +
            "score SMALLINT NOT NULL DEFAULT 0, " +
            "severity VARCHAR(36), " +
            "is_read boolean NOT NULL DEFAULT false, " +
            "timestamp BIGINT NOT NULL, " +
            "server_name VARCHAR(255) null, " +
            "is_expired boolean NOT NULL DEFAULT false, " +
            "player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown', " +
            "warner_name VARCHAR(32) NOT NULL DEFAULT 'Unknown')";
    }

    @Override
    public int getVersion() {
        return 84;
    }
}

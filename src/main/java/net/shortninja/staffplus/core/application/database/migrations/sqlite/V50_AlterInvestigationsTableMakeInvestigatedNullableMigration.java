package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class V50_AlterInvestigationsTableMakeInvestigatedNullableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.asList(
            createBackupTable(),
            "INSERT INTO sp_investigations_backup SELECT ID,investigator_uuid,investigated_uuid,status,server_name,creation_timestamp,conclusion_timestamp FROM sp_investigations;",
            "DROP TABLE sp_investigations;",
            createNewInvestigationsTable(),
            "INSERT INTO sp_investigations SELECT ID,investigator_uuid,investigated_uuid,status,server_name,creation_timestamp,conclusion_timestamp FROM sp_investigations_backup;",
            "DROP TABLE sp_investigations_backup;");
    }

    @NotNull
    private String createNewInvestigationsTable() {
        return "CREATE TABLE sp_investigations (  " +
        "ID integer PRIMARY KEY,  " +
        "investigator_uuid VARCHAR(36) NOT NULL,  " +
        "investigated_uuid VARCHAR(36) NULL,  " +
        "status VARCHAR(36) NOT NULL DEFAULT 'OPEN',  " +
        "server_name VARCHAR(255) NULL,  " +
        "creation_timestamp BIGINT NOT NULL, " +
        "conclusion_timestamp BIGINT NULL);";
    }

    @NotNull
    private String createBackupTable() {
        return "CREATE TABLE sp_investigations_backup (  " +
            "ID integer PRIMARY KEY,  " +
            "investigator_uuid VARCHAR(36) NOT NULL,  " +
            "investigated_uuid VARCHAR(36) NULL,  " +
            "status VARCHAR(36) NOT NULL DEFAULT 'OPEN',  " +
            "server_name VARCHAR(255) NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "conclusion_timestamp BIGINT NULL);";
    }

    @Override
    public int getVersion() {
        return 50;
    }
}

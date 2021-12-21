package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class V79_ReworkAppealsTableColumnMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.asList(
            createNewTable(),
            "INSERT INTO sp_appeals SELECT ID, warning_id, appealer_uuid, resolver_uuid, reason, resolve_reason, status, timestamp, 'WARNING' FROM sp_warning_appeals;",
            "DROP TABLE sp_warning_appeals;");
    }

    @NotNull
    private String createNewTable() {
        return "CREATE TABLE IF NOT EXISTS sp_appeals (  " +
            "ID integer PRIMARY KEY,  " +
            "appealable_id integer NOT NULL,  " +
            "appealer_uuid VARCHAR(36) NOT NULL,  " +
            "resolver_uuid VARCHAR(36) NULL,  " +
            "reason TEXT NOT NULL,  " +
            "resolve_reason TEXT NULL,  " +
            "status VARCHAR(36) NOT NULL DEFAULT 'OPEN',  " +
            "timestamp BIGINT NOT NULL," +
            "type VARCHAR(36) NOT NULL);";
    }

    @Override
    public int getVersion() {
        return 79;
    }
}

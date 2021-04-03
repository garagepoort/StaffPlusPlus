package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

public class V48_CreateInvestigationNotesTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_investigation_notes (  " +
            "ID integer PRIMARY KEY,  " +
            "investigation_id integer NOT NULL,  " +
            "note TEXT NOT NULL,  " +
            "noted_by_uuid VARCHAR(36) NOT NULL,  " +
            "timestamp BIGINT NOT NULL, " +
            "FOREIGN KEY (investigation_id) REFERENCES sp_investigations(id) ON DELETE CASCADE" +
            ");";
    }

    @Override
    public int getVersion() {
        return 48;
    }
}

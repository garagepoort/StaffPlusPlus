package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V48_CreateInvestigationNotesTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_investigation_notes (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "investigation_id INT NOT NULL,  " +
            "note TEXT NOT NULL,  " +
            "noted_by_uuid VARCHAR(36) NOT NULL,  " +
            "timestamp BIGINT NOT NULL, " +
            "FOREIGN KEY (investigation_id) REFERENCES sp_investigations(id) ON DELETE CASCADE, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 48;
    }
}

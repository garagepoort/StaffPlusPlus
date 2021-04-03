package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V47_AlterInvestigationEvidenceTableAddDescriptionColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_investigation_evidence ADD COLUMN description TEXT NOT NULL;";
    }

    @Override
    public int getVersion() {
        return 47;
    }
}

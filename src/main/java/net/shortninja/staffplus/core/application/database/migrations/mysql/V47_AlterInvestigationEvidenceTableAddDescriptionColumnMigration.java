package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V47_AlterInvestigationEvidenceTableAddDescriptionColumnMigration implements Migration {

    @Override
    public String getStatement(Connection connection) {
        return "ALTER TABLE sp_investigation_evidence ADD COLUMN description TEXT NOT NULL;";
    }

    @Override
    public int getVersion() {
        return 47;
    }
}
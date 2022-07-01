package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V46_CreateInvestigationEvidenceTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_investigation_evidence (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "investigation_id INT NOT NULL,  " +
            "evidence_id INT NOT NULL,  " +
            "evidence_type VARCHAR(36) NOT NULL,  " +
            "linked_by_uuid VARCHAR(36) NOT NULL,  " +
            "timestamp BIGINT NOT NULL, " +
            "FOREIGN KEY (investigation_id) REFERENCES sp_investigations(id) ON DELETE CASCADE, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 46;
    }
}

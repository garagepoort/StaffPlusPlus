package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V12_AlterReportTablePlayerUuidNullableMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports MODIFY COLUMN Player_UUID varchar(36);";
    }

    @Override
    public int getVersion() {
        return 12;
    }
}

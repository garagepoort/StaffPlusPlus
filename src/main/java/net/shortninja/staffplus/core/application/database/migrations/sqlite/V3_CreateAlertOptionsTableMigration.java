package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V3_CreateAlertOptionsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_alert_options ( Name_Change VARCHAR(5) NULL,  Mention VARCHAR(5) NULL,  Xray VARCHAR(5) NULL,  Player_UUID VARCHAR(36) PRIMARY KEY);";
    }

    @Override
    public int getVersion() {
        return 3;
    }
}

package net.shortninja.staffplus.core.application.database.migrations.mysql;

import net.shortninja.staffplus.core.application.database.migrations.Migration;

public class V3_CreateAlertOptionsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_alert_options ( Name_Change VARCHAR(5) NULL,  Mention VARCHAR(5) NULL,  Xray VARCHAR(5) NULL,  Player_UUID VARCHAR(36) NOT NULL,  PRIMARY KEY (Player_UUID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 3;
    }
}

package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V26_CreateMutedPlayersTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_muted_players (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "player_uuid VARCHAR(36) NOT NULL,  " +
            "issuer_uuid VARCHAR(36) NOT NULL,  " +
            "unmuted_by_uuid VARCHAR(36) NULL,  " +
            "reason TEXT NOT NULL,  " +
            "unmute_reason TEXT NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "end_timestamp BIGINT NULL,  " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 26;
    }
}

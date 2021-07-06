package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V62_CreateBannedIpsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_banned_ips (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "ip VARCHAR(15) NOT NULL,  " +
            "issuer_name VARCHAR(36) NOT NULL,  " +
            "issuer_uuid VARCHAR(36) NOT NULL,  " +
            "unbanned_by_uuid VARCHAR(36) NULL,  " +
            "unbanned_by_name VARCHAR(36) NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "end_timestamp BIGINT NULL,  " +
            "server_name VARCHAR(255) NULL, " +
            "silent_ban boolean NOT NULL, " +
            "silent_unban boolean NULL, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 62;
    }
}

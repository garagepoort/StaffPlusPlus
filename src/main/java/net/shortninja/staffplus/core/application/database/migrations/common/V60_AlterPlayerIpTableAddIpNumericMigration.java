package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import java.util.Arrays;
import java.util.List;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V60_AlterPlayerIpTableAddIpNumericMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        return Arrays.asList("ALTER TABLE sp_player_ips ADD COLUMN ip_numeric BIGINT; CREATE INDEX sp_player_ips_ipnumeric ON sp_player_ips(ip_numeric);");
    }

    @Override
    public int getVersion() {
        return 60;
    }
}

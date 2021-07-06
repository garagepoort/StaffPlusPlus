package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

import java.util.Arrays;
import java.util.List;

public class V60_AlterPlayerIpTableAddIpNumericMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.asList("ALTER TABLE sp_player_ips ADD COLUMN ip_numeric BIGINT; CREATE INDEX sp_player_ips_ipnumeric ON sp_player_ips(ip_numeric);");
    }

    @Override
    public int getVersion() {
        return 60;
    }
}

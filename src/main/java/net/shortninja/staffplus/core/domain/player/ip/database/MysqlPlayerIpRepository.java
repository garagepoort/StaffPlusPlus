package net.shortninja.staffplus.core.domain.player.ip.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlPlayerIpRepository extends AbstractSqlPlayerIpRepository {

    public MysqlPlayerIpRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }
}

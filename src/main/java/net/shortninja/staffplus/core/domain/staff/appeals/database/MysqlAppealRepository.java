package net.shortninja.staffplus.core.domain.staff.appeals.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlAppealRepository extends AbstractSqlAppealRepository {

    public MysqlAppealRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider) {
        super(playerManager, sqlConnectionProvider);
    }
}

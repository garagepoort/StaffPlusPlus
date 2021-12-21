package net.shortninja.staffplus.core.domain.staff.appeals.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteAppealRepository extends AbstractSqlAppealRepository {

    public SqliteAppealRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider) {
        super(playerManager, sqlConnectionProvider);
    }
}

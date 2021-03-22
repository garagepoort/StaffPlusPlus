package net.shortninja.staffplus.core.domain.staff.warn.appeals.database;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteAppealRepository extends AbstractSqlAppealRepository {

    public SqliteAppealRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider) {
        super(playerManager, sqlConnectionProvider);
    }
}

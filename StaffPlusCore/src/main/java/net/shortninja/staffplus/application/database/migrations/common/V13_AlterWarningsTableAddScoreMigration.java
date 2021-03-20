package net.shortninja.staffplus.application.database.migrations.common;

import net.shortninja.staffplus.application.database.migrations.Migration;

public class V13_AlterWarningsTableAddScoreMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_warnings ADD COLUMN score SMALLINT NOT NULL DEFAULT 0;";
    }

    @Override
    public int getVersion() {
        return 13;
    }
}

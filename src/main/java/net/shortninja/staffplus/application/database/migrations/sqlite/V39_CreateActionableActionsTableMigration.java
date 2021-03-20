package net.shortninja.staffplus.application.database.migrations.sqlite;

import net.shortninja.staffplus.application.database.migrations.Migration;

public class V39_CreateActionableActionsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_actionable_actions (  " +
            "ID integer PRIMARY KEY,  " +
            "actionable_id integer NOT NULL,  " +
            "actionable_type VARCHAR(36) NOT NULL,  " +
            "command VARCHAR(255) NOT NULL,  " +
            "rollback_command VARCHAR(255) NULL,  " +
            "run_strategy VARCHAR(255) NOT NULL,  " +
            "rollback_run_strategy VARCHAR(255) NULL,  " +
            "execution_timestamp BIGINT NULL," +
            "rollback_timestamp BIGINT NULL" +
            ");";
    }

    @Override
    public int getVersion() {
        return 39;
    }
}

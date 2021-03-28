package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V44_AlterSessionsTableAddStaffChatMutedMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_sessions ADD COLUMN staff_chat_muted boolean NOT NULL DEFAULT false;";
    }

    @Override
    public int getVersion() {
        return 44;
    }
}

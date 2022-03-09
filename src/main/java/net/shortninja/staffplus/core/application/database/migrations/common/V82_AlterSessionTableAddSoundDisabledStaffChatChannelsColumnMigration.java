package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V82_AlterSessionTableAddSoundDisabledStaffChatChannelsColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_sessions ADD COLUMN sound_disabled_staff_chat_channels VARCHAR(255) null;";
    }

    @Override
    public int getVersion() {
        return 57;
    }
}

package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V83_AlterSessionTableAddSoundDisabledStaffChatChannelsColumnMigration implements Migration {

    @Override
    public String getStatement(Connection connection) {
        return "ALTER TABLE sp_sessions ADD COLUMN sound_disabled_staff_chat_channels VARCHAR(255) null;";
    }

    @Override
    public int getVersion() {
        return 83;
    }
}

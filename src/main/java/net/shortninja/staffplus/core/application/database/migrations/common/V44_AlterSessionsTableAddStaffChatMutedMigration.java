package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V44_AlterSessionsTableAddStaffChatMutedMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "ALTER TABLE sp_sessions ADD COLUMN staff_chat_muted boolean NOT NULL default 0;";
    }

    @Override
    public int getVersion() {
        return 44;
    }
}

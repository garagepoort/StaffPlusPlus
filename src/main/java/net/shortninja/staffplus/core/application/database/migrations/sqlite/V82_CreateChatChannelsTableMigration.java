package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import java.util.Arrays;
import java.util.List;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V82_CreateChatChannelsTableMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        String chatChannels = "CREATE TABLE IF NOT EXISTS sp_chat_channels (  " +
            "ID integer PRIMARY KEY,  " +
            "chat_prefix VARCHAR(255) NOT NULL, " +
            "chat_line VARCHAR(255) NOT NULL, " +
            "channel_name VARCHAR(255) NOT NULL, " +
            "channel_id VARCHAR(36) NOT NULL, " +
            "type VARCHAR(36) NOT NULL, " +
            "server_name VARCHAR(255) NOT NULL);";
        String members = "CREATE TABLE IF NOT EXISTS sp_chat_channel_members (  " +
            "ID integer PRIMARY KEY,  " +
            "channel_id INT NOT NULL, " +
            "player_uuid VARCHAR(36) NOT NULL);";
        return Arrays.asList(chatChannels, members);
    }

    @Override
    public int getVersion() {
        return 82;
    }
}

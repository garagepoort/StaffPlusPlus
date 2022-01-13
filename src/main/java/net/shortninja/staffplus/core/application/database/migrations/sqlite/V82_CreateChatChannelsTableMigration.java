package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import java.util.Arrays;
import java.util.List;

public class V82_CreateChatChannelsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        String chatChannels = "CREATE TABLE IF NOT EXISTS sp_chat_channels (  " +
            "ID integer PRIMARY KEY,  " +
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

package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class V82_CreateChatChannelsTableMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        String chatChannels = "CREATE TABLE IF NOT EXISTS sp_chat_channels (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "chat_prefix VARCHAR(255) NOT NULL, " +
            "chat_line VARCHAR(255) NOT NULL, " +
            "channel_name VARCHAR(255) NOT NULL, " +
            "channel_id VARCHAR(36) NOT NULL, " +
            "type VARCHAR(36) NOT NULL, " +
            "server_name VARCHAR(255) NOT NULL, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
        String members = "CREATE TABLE IF NOT EXISTS sp_chat_channel_members (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "channel_id INT NOT NULL, " +
            "player_uuid VARCHAR(36) NOT NULL, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
        return Arrays.asList(chatChannels, members);
    }

    @Override
    public int getVersion() {
        return 82;
    }
}

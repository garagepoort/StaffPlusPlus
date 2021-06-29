package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StaffChatMessageFormatMigrator implements ConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "config");
        FileConfiguration langConfig = getConfig(configs, config.getString("lang"));

        if(langConfig.contains("staff-chat")) {
            String messageFormat = langConfig.getString("staff-chat");
            List<LinkedHashMap<String, Object>> channels = (List<LinkedHashMap<String, Object>>) config.getList("staff-chat-module.channels", new ArrayList<>());
            channels.forEach(map -> map.put("message-format", messageFormat));
            config.set("staff-chat-module.channels", channels);
            langConfig.set("staff-chat", null);
        }
    }
}

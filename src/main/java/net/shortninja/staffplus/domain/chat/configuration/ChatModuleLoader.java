package net.shortninja.staffplus.domain.chat.configuration;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ChatModuleLoader extends ConfigLoader<ChatConfiguration> {
    @Override
    protected ChatConfiguration load(FileConfiguration config) {
        boolean chatEnabled = config.getBoolean("chat-module.enabled");
        int chatLines = config.getInt("chat-module.lines");
        int chatSlow = config.getInt("chat-module.slow");
        List<String> detectionPhrases = config.getStringList("chat-module.detection.phrases");
        return new ChatConfiguration(chatEnabled, chatLines, chatSlow, detectionPhrases);
    }
}

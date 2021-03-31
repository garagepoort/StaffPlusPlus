package net.shortninja.staffplus.core.domain.chat.configuration;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@IocBean
public class ChatModuleLoader extends AbstractConfigLoader<ChatConfiguration> {
    @Override
    protected ChatConfiguration load(FileConfiguration config) {
        boolean chatEnabled = config.getBoolean("chat-module.enabled");
        int chatLines = config.getInt("chat-module.lines");
        int chatSlow = config.getInt("chat-module.slow");
        List<String> detectionPhrases = config.getStringList("chat-module.detection.phrases");
        return new ChatConfiguration(chatEnabled, chatLines, chatSlow, detectionPhrases);
    }
}

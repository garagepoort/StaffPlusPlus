package net.shortninja.staffplus.core.domain.chat.configuration;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import java.util.List;

@IocBean
public class ChatModuleLoader extends AbstractConfigLoader<ChatConfiguration> {
    @Override
    protected ChatConfiguration load() {
        boolean chatEnabled = defaultConfig.getBoolean("chat-module.enabled");
        int chatLines = defaultConfig.getInt("chat-module.lines");
        int chatSlow = defaultConfig.getInt("chat-module.slow");
        List<String> detectionPhrases = defaultConfig.getStringList("chat-module.detection.phrases");
        return new ChatConfiguration(chatEnabled, chatLines, chatSlow, detectionPhrases);
    }
}

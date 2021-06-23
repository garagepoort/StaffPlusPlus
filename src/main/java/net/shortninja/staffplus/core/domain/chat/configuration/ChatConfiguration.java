package net.shortninja.staffplus.core.domain.chat.configuration;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

import java.util.List;

@IocBean
public class ChatConfiguration {

    @ConfigProperty("chat-module.enabled")
    private boolean chatEnabled;
    @ConfigProperty("chat-module.lines")
    private int chatLines;
    @ConfigProperty("chat-module.slow")
    private int chatSlow;
    @ConfigProperty("chat-module.detection.phrases")
    private List<String> detectionPhrases;

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public int getChatLines() {
        return chatLines;
    }

    public int getChatSlow() {
        return chatSlow;
    }

    public List<String> getDetectionPhrases() {
        return detectionPhrases;
    }
}

package net.shortninja.staffplus.server.chat.configuration;

import java.util.List;

public class ChatConfiguration {

    private boolean chatEnabled;
    private int chatLines;
    private int chatSlow;
    private List<String> detectionPhrases;

    public ChatConfiguration(boolean chatEnabled, int chatLines, int chatSlow, List<String> detectionPhrases) {
        this.chatEnabled = chatEnabled;
        this.chatLines = chatLines;
        this.chatSlow = chatSlow;
        this.detectionPhrases = detectionPhrases;
    }

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

package net.shortninja.staffplus.core.domain.chat.configuration;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

import java.util.List;

@IocBean
public class ChatConfiguration {

    @ConfigProperty("chat-module.enabled")
    public boolean chatEnabled;
    @ConfigProperty("chat-module.lines")
    public int chatLines;
    @ConfigProperty("chat-module.slow")
    public int chatSlow;
    @ConfigProperty("chat-module.detection.phrases")
    public List<String> detectionPhrases;
}

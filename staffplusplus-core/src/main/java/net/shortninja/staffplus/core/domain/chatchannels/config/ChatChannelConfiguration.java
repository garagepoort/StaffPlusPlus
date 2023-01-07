package net.shortninja.staffplus.core.domain.chatchannels.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigurationLoader;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;

import java.util.ArrayList;
import java.util.List;

@IocBean
public class ChatChannelConfiguration {

    public List<ChatChannelType> enabledChannels;

    public ChatChannelConfiguration(ConfigurationLoader configurationLoader) {
        enabledChannels = new ArrayList<>();
        FileConfiguration defaultConfig = configurationLoader.getConfigurationFiles().get("config");
        if(defaultConfig.getBoolean("reports-module.chatchannels.enabled")) {
            enabledChannels.add(ChatChannelType.REPORT);
        }
        if(defaultConfig.getBoolean("freeze-module.chatchannels.enabled")) {
            enabledChannels.add(ChatChannelType.FREEZE);
        }
    }
}

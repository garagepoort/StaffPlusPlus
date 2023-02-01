package net.shortninja.staffplus.core.staffchat.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigObjectList;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.staffchat.StaffChatChannelConfiguration;

import java.util.ArrayList;
import java.util.List;

@IocBean
public class StaffChatConfiguration {

    @ConfigProperty("staff-chat-module.enabled")
    private boolean enabled;
    @ConfigProperty("staff-chat-module.bungee")
    private boolean bungeeEnabled;
    @ConfigProperty("staff-chat-module.channels")
    @ConfigObjectList(StaffChatChannelConfiguration.class)
    private List<StaffChatChannelConfiguration> channelConfigurations = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isBungeeEnabled() {
        return bungeeEnabled;
    }

    public List<StaffChatChannelConfiguration> getChannelConfigurations() {
        return channelConfigurations;
    }
}

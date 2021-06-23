package net.shortninja.staffplus.core.domain.staff.staffchat.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigListTransformer;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.domain.staff.staffchat.StaffChatChannelConfiguration;

import java.util.List;

@IocBean
public class StaffChatConfiguration {

    @ConfigProperty("staff-chat-module.enabled")
    private boolean enabled;
    @ConfigProperty("staff-chat-module.bungee")
    private boolean bungeeEnabled;
    @ConfigProperty("staff-chat-module.channels")
    @ConfigListTransformer(StaffChatChannelConfigMapper.class)
    private List<StaffChatChannelConfiguration> channelConfigurations;

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

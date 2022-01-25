package net.shortninja.staffplus.core.domain.chatchannels.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@IocBean
public class ChatChannelConfiguration {

    public List<ChatChannelType> enabledChannels;

    public ChatChannelConfiguration() {
        enabledChannels = new ArrayList<>();
        FileConfiguration defaultConfig = StaffPlus.get().getFileConfigurations().get("config");
        if(defaultConfig.getBoolean("reports-module.chatchannels.enabled")) {
            enabledChannels.add(ChatChannelType.REPORT);
        }
        if(defaultConfig.getBoolean("freeze-module.chatchannels.enabled")) {
            enabledChannels.add(ChatChannelType.FREEZE);
        }
    }
}

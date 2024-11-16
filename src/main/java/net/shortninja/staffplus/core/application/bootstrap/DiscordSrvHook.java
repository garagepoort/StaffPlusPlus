package net.shortninja.staffplus.core.application.bootstrap;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeDiscordSrvListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@IocBean
public class DiscordSrvHook {
    
    private StaffModeDiscordSrvListener staffListener = null;
    
    public DiscordSrvHook(OnlineSessionsManager sessionManager) {
        Plugin discordSrvPlugin = Bukkit.getPluginManager().getPlugin("DiscordSRV");
        
        if (discordSrvPlugin != null && discordSrvPlugin.isEnabled()) {
            staffListener = new StaffModeDiscordSrvListener(sessionManager);
        }
    }
}

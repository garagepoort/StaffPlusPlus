package net.shortninja.staffplus.core.application.bootstrap;

import be.garagepoort.mcioc.IocBean;
import github.scarsz.discordsrv.DiscordSRV;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeDiscordSrvListener;
import net.shortninja.staffplus.core.domain.staff.vanish.listeners.VanishJoinLeaveDiscordSrvMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@IocBean
public class DiscordSrvHook {
    
    private StaffModeDiscordSrvListener staffListener;
    private VanishJoinLeaveDiscordSrvMessageListener vanishMessageListener;
    
    public DiscordSrvHook(OnlineSessionsManager sessionManager) {
        Plugin discordSrvPlugin = Bukkit.getPluginManager().getPlugin("DiscordSRV");
        
        if (!(discordSrvPlugin != null && discordSrvPlugin.isEnabled())) {
            return;
        }
        
        staffListener = new StaffModeDiscordSrvListener(sessionManager);
        vanishMessageListener = new VanishJoinLeaveDiscordSrvMessageListener();
        
        DiscordSRV.api.subscribe(staffListener);
        
        
        StaffPlusPlus.get().getLogger().info("Hooked into DiscordSRV " + discordSrvPlugin.getDescription().getVersion());
    }
}

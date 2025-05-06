package net.shortninja.staffplus.core.domain.staff.mode;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.AchievementMessagePreProcessEvent;
import github.scarsz.discordsrv.api.events.DeathMessagePreProcessEvent;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class StaffModeDiscordSrvListener {
    private final OnlineSessionsManager sessionManager;
    
    public StaffModeDiscordSrvListener(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    @Subscribe(priority = ListenerPriority.HIGH)
    public void onAchievementPreProcess(AchievementMessagePreProcessEvent event) {
        Player player = event.getPlayer();
        
        if (!sessionManager.has(player.getUniqueId())) return;
        OnlinePlayerSession session = sessionManager.get(player);
        
        if (!(session.isInStaffMode() || session.isVanished())) return;
        event.setCancelled(true);
    }
    
    @Subscribe(priority = ListenerPriority.HIGH)
    public void onDeathPreProcess(DeathMessagePreProcessEvent event) {
        Player player = event.getPlayer();
        
        if (!sessionManager.has(player.getUniqueId())) return;
        OnlinePlayerSession session = sessionManager.get(player);
        
        if (!(session.isInStaffMode() || session.isVanished())) return;
        event.setCancelled(true);
    }
}
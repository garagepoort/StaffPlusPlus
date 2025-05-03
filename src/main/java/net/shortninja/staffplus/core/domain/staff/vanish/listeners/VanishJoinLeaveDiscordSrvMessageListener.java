package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import github.scarsz.discordsrv.DiscordSRV;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

@IocBean
public class VanishJoinLeaveDiscordSrvMessageListener implements Listener {
    
    @ConfigProperty("vanish-module.join-leave-message-enabled")
    private boolean vanishMessagesEnabled;
    @ConfigProperty("%lang%:vanish-join-message")
    private String vanishJoinMessage;
    @ConfigProperty("%lang%:vanish-leave-message")
    private String vanishLeaveMessage;
    
    public VanishJoinLeaveDiscordSrvMessageListener() {
        Plugin discordSrvPlugin = Bukkit.getPluginManager().getPlugin("DiscordSRV");
        if (!(discordSrvPlugin != null && discordSrvPlugin.isEnabled())) {
            return;
        }
        
        StaffPlusPlus.get().getServer().getPluginManager().registerEvents(this, StaffPlusPlus.get());
    }
    
    @EventHandler
    public void onVanish(VanishOnEvent event) {
        if (!vanishMessagesEnabled || event.isOnJoin() || !(event.getType() == VanishType.LIST || event.getType() == VanishType.TOTAL)) {
            return;
        }
        
        DiscordSRV.getPlugin().sendLeaveMessage(event.getPlayer(), vanishLeaveMessage.replace("%player%", event.getPlayer().getName()));
    }

    @EventHandler
    public void onUnvanish(VanishOffEvent event) {
        if (!vanishMessagesEnabled || !(event.getType() == VanishType.LIST || event.getType() == VanishType.TOTAL)) {
            return;
        }
        
        DiscordSRV.getPlugin().sendJoinMessage(event.getPlayer(), vanishJoinMessage.replace("%player%", event.getPlayer().getName()));
    }
}

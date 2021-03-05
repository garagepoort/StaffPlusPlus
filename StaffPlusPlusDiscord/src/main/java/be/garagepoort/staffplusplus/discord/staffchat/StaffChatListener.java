package be.garagepoort.staffplusplus.discord.staffchat;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import be.garagepoort.staffplusplus.discord.StaffPlusPlusListener;
import github.scarsz.discordsrv.DiscordSRV;
import net.shortninja.staffplusplus.IStaffPlus;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.RegisteredServiceProvider;

public class StaffChatListener implements StaffPlusPlusListener {

    private final FileConfiguration config;
    private final StaffPlusPlusDiscord staffPlusPlusDiscord;
    private IStaffPlus staffPlus;

    public StaffChatListener(FileConfiguration config, StaffPlusPlusDiscord staffPlusPlusDiscord) {
        this.config = config;
        this.staffPlusPlusDiscord = staffPlusPlusDiscord;
        RegisteredServiceProvider<IStaffPlus> provider = Bukkit.getServicesManager().getRegistration(IStaffPlus.class);
        if (provider != null) {
            this.staffPlus = provider.getProvider();
        }
    }

    public void init() {
        if (staffPlusPlusDiscord.getServer().getPluginManager().isPluginEnabled("DiscordSRV")) {
            DiscordSRV.api.subscribe(new DiscordStaffChatListener(staffPlusPlusDiscord, staffPlus));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleStaffChatEvent(StaffChatEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.staffchat.sync")) {
            return;
        }
        // Send to discord off the main thread (just like DiscordSRV does)
        staffPlusPlusDiscord.getServer().getScheduler().runTaskAsynchronously(staffPlusPlusDiscord, () ->
            DiscordSRV.getPlugin().processChatMessage(event.getPlayer(), event.getMessage(), DiscordStaffChatListener.CHANNEL, false)
        );
    }

    @Override
    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.staffchat.sync") && staffPlus != null;
    }

    @Override
    public boolean isValid() {
        return staffPlusPlusDiscord.getServer().getPluginManager().isPluginEnabled("DiscordSRV");
    }
}

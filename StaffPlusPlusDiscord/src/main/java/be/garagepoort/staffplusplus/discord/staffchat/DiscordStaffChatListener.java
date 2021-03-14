package be.garagepoort.staffplusplus.discord.staffchat;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import net.shortninja.staffplusplus.IStaffPlus;

import java.util.Objects;

public class DiscordStaffChatListener {
    private final StaffPlusPlusDiscord plugin;
    private final IStaffPlus staffPlus;
    public static final String CHANNEL = "staffplusplus-staffchat";

    public DiscordStaffChatListener(StaffPlusPlusDiscord plugin, IStaffPlus staffPlus) {
        this.plugin = plugin;
        this.staffPlus = staffPlus;
    }

    @Subscribe
    public void onDiscordChat(DiscordGuildMessagePostProcessEvent event) {
        if (event.getChannel().equals(getDiscordChannel())) {
            event.setCancelled(true); // Cancel this message from getting sent to global chat.

            // Handle this on the main thread next tick.
            plugin.getServer().getScheduler().runTask(plugin, () ->
                submitMessageFromDiscord(event.getAuthor(), event.getProcessedMessage())
            );
        }
    }

    public TextChannel getDiscordChannel() {
        TextChannel destinationTextChannelForGameChannelName = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(CHANNEL);
        if (destinationTextChannelForGameChannelName == null) {
            throw new RuntimeException("DiscordSRV not setup correctly. No channel configured with name [" + CHANNEL + "]");
        }
        return destinationTextChannelForGameChannelName;
    }

    public void submitMessageFromDiscord(User author, String message)
    {
        Objects.requireNonNull(author, "author");
        Objects.requireNonNull(message, "message");
        staffPlus.getStaffChatService().sendMessage(message);
    }
}

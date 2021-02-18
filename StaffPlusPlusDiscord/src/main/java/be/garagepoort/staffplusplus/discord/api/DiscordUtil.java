package be.garagepoort.staffplusplus.discord.api;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import org.bukkit.Bukkit;

public class DiscordUtil {

    public static DiscordMessageFooter createFooter() {
        return new DiscordMessageFooter("Provided by Staff++", "https://cdn.discordapp.com/embed/avatars/0.png");
    }

    public static void sendEvent(DiscordClient client, String template) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlusDiscord.get(), () -> {
            client.sendTemplate(template);
        });
    }
}

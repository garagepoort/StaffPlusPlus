package be.garagepoort.staffplusplus.discord.api;

import be.garagepoort.staffplusplus.discord.Constants;
import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class DiscordUtil {

    public static DiscordMessageFooter createFooter() {
        return new DiscordMessageFooter("Provided by Staff++", "https://cdn.discordapp.com/embed/avatars/0.png");
    }

    public static void sendEvent(DiscordClient client, String content, String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlusDiscord.get(), () -> {
            client.sendEvent(new DiscordMessage("Alt Account detection from Staff++", new DiscordMessageEmbed(
                title,
                Constants.STAFFPLUSPLUS_URL,
                color,
                time,
                DiscordUtil.createFooter(),
                fields
            )));
        });
    }
}

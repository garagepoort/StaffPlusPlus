package be.garagepoort.staffplusplus.discord.api;

import java.util.Collections;
import java.util.List;

public class DiscordMessage {

    private String content;
    private List<DiscordMessageEmbed> embeds;

    public DiscordMessage(String content, DiscordMessageEmbed embed) {
        this.content = content;
        this.embeds = Collections.singletonList(embed);
    }

    public List<DiscordMessageEmbed> getEmbeds() {
        return embeds;
    }

    public String getContent() {
        return content;
    }
}

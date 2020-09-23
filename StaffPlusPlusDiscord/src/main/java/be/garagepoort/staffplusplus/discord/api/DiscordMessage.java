package be.garagepoort.staffplusplus.discord.api;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class DiscordMessage {

    private String content;
    private File file;
    private List<DiscordMessageEmbed> embeds;

    public DiscordMessage(String content, DiscordMessageEmbed embed) {
        this.content = content;
        this.embeds = Collections.singletonList(embed);
    }

    public DiscordMessage(String content, File file, DiscordMessageEmbed embed) {
        this.content = content;
        this.file = file;
        this.embeds = Collections.singletonList(embed);
    }

    public List<DiscordMessageEmbed> getEmbeds() {
        return embeds;
    }

    public String getContent() {
        return content;
    }

    public File getFile() {
        return file;
    }
}

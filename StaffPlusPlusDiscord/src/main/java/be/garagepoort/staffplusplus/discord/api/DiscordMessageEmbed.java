package be.garagepoort.staffplusplus.discord.api;

import java.util.List;

public class DiscordMessageEmbed {

    private String title;
    private String url;
    //colors: open: 6431896 in_progress: 16620323 rejected: 16601379 resolved: 5027875
    private String color;
    private String timestamp;
    private DiscordMessageFooter footer;
    private List<DiscordMessageField> fields;

    public DiscordMessageEmbed(String title, String url, String color, String timestamp, DiscordMessageFooter footer, List<DiscordMessageField> fields) {
        this.title = title;
        this.url = url;
        this.color = color;
        this.timestamp = timestamp;
        this.footer = footer;
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getColor() {
        return color;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public DiscordMessageFooter getFooter() {
        return footer;
    }

    public List<DiscordMessageField> getFields() {
        return fields;
    }
}

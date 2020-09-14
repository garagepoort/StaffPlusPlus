package be.garagepoort.staffplusplus.discord.api;

public class DiscordMessageFooter {

    private String text;
    private String icon_url;

    public DiscordMessageFooter(String text, String icon_url) {
        this.text = text;
        this.icon_url = icon_url;
    }

    public String getText() {
        return text;
    }

    public String getIcon_url() {
        return icon_url;
    }
}

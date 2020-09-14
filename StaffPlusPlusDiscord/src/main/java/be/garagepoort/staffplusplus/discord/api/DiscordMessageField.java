package be.garagepoort.staffplusplus.discord.api;

public class DiscordMessageField {

    private String name;
    private String value;
    private boolean inline;

    public DiscordMessageField(String name, String value, boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    public DiscordMessageField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isInline() {
        return inline;
    }
}

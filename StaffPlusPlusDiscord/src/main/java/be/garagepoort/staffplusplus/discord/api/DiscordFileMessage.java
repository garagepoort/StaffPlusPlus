package be.garagepoort.staffplusplus.discord.api;

public class DiscordFileMessage {

    private byte[] file1;
    private DiscordMessage payload_json;

    public DiscordFileMessage(byte[] file1, DiscordMessage payload_json) {
        this.file1 = file1;
        this.payload_json = payload_json;
    }
}

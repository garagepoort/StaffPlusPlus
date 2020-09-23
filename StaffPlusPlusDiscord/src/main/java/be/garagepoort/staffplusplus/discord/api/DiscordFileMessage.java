package be.garagepoort.staffplusplus.discord.api;

import java.io.File;

public class DiscordFileMessage {

    private File file1;
    private DiscordMessage payload;

    public DiscordFileMessage(File file1, DiscordMessage payload) {
        this.file1 = file1;
        this.payload = payload;
    }
}

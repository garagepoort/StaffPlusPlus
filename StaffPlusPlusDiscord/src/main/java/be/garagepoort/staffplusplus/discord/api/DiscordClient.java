package be.garagepoort.staffplusplus.discord.api;

import feign.Headers;
import feign.RequestLine;

public interface DiscordClient {

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    void sendEvent(DiscordMessage discordMessage);

    @RequestLine("POST")
    @Headers("Content-Type: multipart/form-data")
    void sendFileEvent(DiscordFileMessage discordMessage);
}

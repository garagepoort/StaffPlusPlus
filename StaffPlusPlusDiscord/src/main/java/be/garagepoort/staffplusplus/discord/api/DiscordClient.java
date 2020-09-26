package be.garagepoort.staffplusplus.discord.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.form.FormData;

public interface DiscordClient {

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    void sendEvent(DiscordMessage discordMessage);

    @RequestLine("POST")
    @Headers("Content-Type: multipart/form-data")
    void sendFileEvent(@Param("payload_json") String payload, @Param("file") FormData file);
}

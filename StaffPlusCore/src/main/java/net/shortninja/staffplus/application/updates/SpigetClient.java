package net.shortninja.staffplus.application.updates;

import feign.Headers;
import feign.RequestLine;

import java.util.List;

public interface SpigetClient {

    @RequestLine("GET /versions?size=5&sort=-releaseDate")
    @Headers("Content-Type: application/json")
    List<ResourceVersion> getVersions();

}

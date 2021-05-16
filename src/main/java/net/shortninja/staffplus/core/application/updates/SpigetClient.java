package net.shortninja.staffplus.core.application.updates;

import feign.Headers;
import feign.RequestLine;
import org.json.simple.JSONObject;

public interface SpigetClient {

    @RequestLine("GET /latest_versions.json")
    @Headers("Content-Type: application/json")
    JSONObject getVersions();

}

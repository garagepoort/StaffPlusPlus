package net.shortninja.staffplus.core.application.updates;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface SpigetClient {

    @GET("latest_versions.json")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getVersions();

}

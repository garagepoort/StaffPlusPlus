package net.shortninja.staffplus.core.common.bungee;

import be.garagepoort.mcioc.IocBean;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@IocBean
public class GsonParser {

    private final Gson gson;

    public GsonParser() {
        gson = new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY)
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
                Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            })
            .registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, type, jsonDeserializationContext) -> {
                Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
            })
            .create();
    }

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public <T> T  fromJson(String data, Class<T> classOf) {
        return gson.fromJson(data, classOf);
    }
}

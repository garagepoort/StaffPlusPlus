package net.shortninja.staffplus.core.common.bungee;

import be.garagepoort.mcioc.IocBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@IocBean
public class GsonParser {

    private final Gson gson;

    public GsonParser() {
        gson = new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).create();
    }

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public <T> T  fromJson(String data, Class<T> classOf) {
        return gson.fromJson(data, classOf);
    }
}

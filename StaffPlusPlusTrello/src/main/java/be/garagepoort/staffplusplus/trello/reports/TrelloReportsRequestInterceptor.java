package be.garagepoort.staffplusplus.trello.reports;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.bukkit.configuration.file.FileConfiguration;

public class TrelloReportsRequestInterceptor implements RequestInterceptor {


    private final FileConfiguration config;

    public TrelloReportsRequestInterceptor(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.query("key", config.getString("StaffPlusPlusTrello.reports.apiKey"));
        requestTemplate.query("token", config.getString("StaffPlusPlusTrello.reports.userToken"));
    }
}

package net.shortninja.staffplus.core.domain.actions.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.application.config.ConfigurationUtil;

import java.util.Map;

public class FiltersTransformer implements IConfigTransformer<Map<String, String>, String> {
    @Override
    public Map<String, String> mapConfig(String filterString) {
        return ConfigurationUtil.loadFilters(filterString);
    }
}

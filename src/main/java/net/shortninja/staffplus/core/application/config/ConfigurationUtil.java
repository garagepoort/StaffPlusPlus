package net.shortninja.staffplus.core.application.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationUtil {

    private ConfigurationUtil() {
    }

    public static Map<String, String> loadFilters(String filtersString) {
        Map<String, String> filterMap = new HashMap<>();
        if (filtersString != null) {
            String[] split = filtersString.split(";");
            for (String filter : split) {
                String[] filterPair = filter.split("=");
                filterMap.put(filterPair[0].toLowerCase(), filterPair[1].toLowerCase());
            }
        }
        return filterMap;
    }
}

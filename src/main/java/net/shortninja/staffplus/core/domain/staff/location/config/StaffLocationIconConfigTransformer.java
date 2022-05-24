package net.shortninja.staffplus.core.domain.staff.location.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StaffLocationIconConfigTransformer implements IConfigTransformer<List<StaffLocationIconConfig>, List<LinkedHashMap<String, Object>>> {

    public String permission;
    public String message;

    @Override
    public List<StaffLocationIconConfig> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return list.stream()
            .map(map -> new StaffLocationIconConfig((String) map.get("icon"), (String) map.get("text")))
            .collect(Collectors.toList());
    }
}
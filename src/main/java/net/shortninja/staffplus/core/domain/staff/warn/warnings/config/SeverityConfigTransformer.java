package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.common.time.TimeUnit;

import java.util.*;
import java.util.stream.Collectors;

public class SeverityConfigTransformer implements IConfigTransformer<List<WarningSeverityConfiguration>, List<LinkedHashMap<String, Object>>> {

    @Override
    public List<WarningSeverityConfiguration> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return list.stream().map(map -> {
            String name = (String) map.get("name");
            int score = (Integer) map.get("score");
            long expirationDuration = map.containsKey("expiresAfter") ? TimeUnit.getDuration((String) map.get("expiresAfter")) : -1;
            String reason = (String) map.get("reason");
            boolean reasonOverwriteEnabled = map.containsKey("reasonOverwriteEnabled") ? (boolean) map.get("reasonOverwriteEnabled") : false;
            return new WarningSeverityConfiguration(name, score, expirationDuration, reason, reasonOverwriteEnabled);
        }).collect(Collectors.toList());
    }
}

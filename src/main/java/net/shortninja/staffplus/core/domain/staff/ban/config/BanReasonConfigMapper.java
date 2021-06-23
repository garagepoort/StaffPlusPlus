package net.shortninja.staffplus.core.domain.staff.ban.config;

import be.garagepoort.mcioc.configuration.IConfigListTransformer;
import net.shortninja.staffplus.core.domain.staff.ban.BanType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BanReasonConfigMapper implements IConfigListTransformer {
    @Override
    public List mapConfig(List<LinkedHashMap<String, Object>> list) {
        return Objects.requireNonNull(list).stream().map(map -> {
            String name = (String) map.get("name");
            String reason = (String) map.get("reason");
            String template = (String) map.get("template");
            BanType banType = map.containsKey("ban-type") ? BanType.valueOf((String) map.get("ban-type")) : null;
            return new BanReasonConfiguration(name, reason, template, banType);
        }).collect(Collectors.toList());
    }
}

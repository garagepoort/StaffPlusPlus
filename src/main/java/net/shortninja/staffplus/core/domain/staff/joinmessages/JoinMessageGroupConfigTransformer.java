package net.shortninja.staffplus.core.domain.staff.joinmessages;

import be.garagepoort.mcioc.configuration.IConfigTransformer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JoinMessageGroupConfigTransformer implements IConfigTransformer<List<JoinMessageGroup>, List<LinkedHashMap<String, Object>>> {

    public String permission;
    public String message;

    @Override
    public List<JoinMessageGroup> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return list.stream().map(map -> {
            String permission = (String) map.get("permission");
            String message = (String) map.get("message");
            int weight = (int) map.get("weight");
            return new JoinMessageGroup(permission, message, weight);
        }).collect(Collectors.toList());
    }
}

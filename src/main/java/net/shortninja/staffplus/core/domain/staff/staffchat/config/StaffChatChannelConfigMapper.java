package net.shortninja.staffplus.core.domain.staff.staffchat.config;

import be.garagepoort.mcioc.configuration.IConfigListTransformer;
import net.shortninja.staffplus.core.domain.staff.staffchat.StaffChatChannelConfiguration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StaffChatChannelConfigMapper implements IConfigListTransformer {
    @Override
    public List<StaffChatChannelConfiguration> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return Objects.requireNonNull(list).stream().map(map -> {
            String name = (String) map.get("name");
            String command = (String) map.get("command");
            String permission = (String) map.get("permission");
            String prefix = (String) map.get("prefix");
            String handle = (String) map.get("handle");
            String messageFormat = (String) map.get("message-format");
            return new StaffChatChannelConfiguration(name, command, permission, handle, prefix, messageFormat);
        }).collect(Collectors.toList());
    }
}
